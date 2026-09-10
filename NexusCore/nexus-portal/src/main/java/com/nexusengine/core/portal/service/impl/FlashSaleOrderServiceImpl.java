package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.domain.FlashSaleOrderMessage;
import com.nexusengine.core.portal.domain.QueueEnum;
import com.nexusengine.core.portal.service.FlashSaleOrderService;
import com.nexusengine.core.portal.service.UmsMemberService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class FlashSaleOrderServiceImpl implements FlashSaleOrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private UmsMemberService memberService;

    private DefaultRedisScript<Long> stockScript;

    // In-memory bucket cache for user-level rate limiting
    private final ConcurrentHashMap<Long, Bucket> buckets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Lua Script: Checks if stock is enough and user hasn't bought, then decrements. Returns 1 if success, 0 if out of stock, -1 if already bought.
        String lua = "local stock = tonumber(redis.call('get', KEYS[1])); " +
                     "if stock and stock >= tonumber(ARGV[1]) then " +
                     "  local bought = redis.call('sismember', KEYS[2], ARGV[2]); " +
                     "  if bought == 1 then return -1; end; " +
                     "  redis.call('decrby', KEYS[1], tonumber(ARGV[1])); " +
                     "  redis.call('sadd', KEYS[2], ARGV[2]); " +
                     "  return 1; " +
                     "else " +
                     "  return 0; " +
                     "end;";
        stockScript = new DefaultRedisScript<>();
        stockScript.setScriptText(lua);
        stockScript.setResultType(Long.class);
    }

    // Fix 4: Distributed rate limiting using Redis instead of in-memory ConcurrentHashMap
    private boolean isRateLimited(Long memberId) {
        String key = "flash:rate_limit:" + memberId;
        Long current = redisTemplate.opsForValue().increment(key);
        if (current != null && current == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(1));
        }
        return current != null && current > 1;
    }

    @Override
    public CommonResult generateFlashOrder(Long productId, Long flashPromotionId, Long flashPromotionSessionId, Integer quantity) {
        UmsMember currentMember = memberService.getCurrentMember();
        if (currentMember == null) {
            return CommonResult.unauthorized(null);
        }

        // Fix 4: Rate Limiting (API Gateway / User level) using Redis
        if (isRateLimited(currentMember.getId())) {
            return CommonResult.failed("Request too frequent, please try again later.");
        }

        // Fix 3: Redis Gatekeeper: Atomic stock deduction and 1-per-user check
        String stockKey = "flash:stock:" + flashPromotionId + ":" + flashPromotionSessionId + ":" + productId;
        String userSetKey = "flash:users:" + flashPromotionId + ":" + flashPromotionSessionId + ":" + productId;
        Long result = redisTemplate.execute(stockScript, java.util.Arrays.asList(stockKey, userSetKey), String.valueOf(quantity), String.valueOf(currentMember.getId()));
        
        if (result == null || result == 0L) {
            return CommonResult.failed("Flash sale inventory is sold out!");
        } else if (result == -1L) {
            return CommonResult.failed("You have already purchased this flash sale item!");
        }

        // 3. Asynchronous Order Processing (RabbitMQ)
        FlashSaleOrderMessage message = new FlashSaleOrderMessage();
        message.setMemberId(currentMember.getId());
        message.setProductId(productId);
        message.setFlashPromotionId(flashPromotionId);
        message.setFlashPromotionSessionId(flashPromotionSessionId);
        message.setQuantity(quantity);

        amqpTemplate.convertAndSend(QueueEnum.QUEUE_FLASH_ORDER.getExchange(), QueueEnum.QUEUE_FLASH_ORDER.getRouteKey(), message);
        log.info("Flash order message sent to MQ for member: {}, product: {}", currentMember.getId(), productId);

        return CommonResult.success(null, "Order request received, processing in background.");
    }
}
