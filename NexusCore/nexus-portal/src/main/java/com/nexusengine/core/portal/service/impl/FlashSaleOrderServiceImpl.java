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
        // Lua Script: Checks if stock is enough, then decrements. Returns 1 if success, 0 if out of stock.
        String lua = "local stock = tonumber(redis.call('get', KEYS[1])); " +
                     "if stock and stock >= tonumber(ARGV[1]) then " +
                     "  redis.call('decrby', KEYS[1], tonumber(ARGV[1])); " +
                     "  return 1; " +
                     "else " +
                     "  return 0; " +
                     "end;";
        stockScript = new DefaultRedisScript<>();
        stockScript.setScriptText(lua);
        stockScript.setResultType(Long.class);
    }

    private Bucket resolveBucket(Long memberId) {
        return buckets.computeIfAbsent(memberId, key -> {
            // Rate Limit: 1 request per second per user
            Bandwidth limit = Bandwidth.classic(1, Refill.greedy(1, Duration.ofSeconds(1)));
            return Bucket.builder().addLimit(limit).build();
        });
    }

    @Override
    public CommonResult generateFlashOrder(Long productId, Long flashPromotionId, Long flashPromotionSessionId, Integer quantity) {
        UmsMember currentMember = memberService.getCurrentMember();
        if (currentMember == null) {
            return CommonResult.unauthorized(null);
        }

        // 1. Rate Limiting (API Gateway / User level)
        Bucket bucket = resolveBucket(currentMember.getId());
        if (!bucket.tryConsume(1)) {
            return CommonResult.failed("Request too frequent, please try again later.");
        }

        // 2. Redis Gatekeeper: Atomic stock deduction
        String stockKey = "flash:stock:" + flashPromotionId + ":" + flashPromotionSessionId + ":" + productId;
        Long result = redisTemplate.execute(stockScript, Collections.singletonList(stockKey), String.valueOf(quantity));
        
        if (result == null || result == 0L) {
            return CommonResult.failed("Flash sale inventory is sold out!");
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
