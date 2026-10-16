package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.common.api.ResultCode;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.FlashOrderResponse;
import com.nexusengine.core.portal.ordering.dto.FlashSaleOrderMessage;
import com.nexusengine.core.portal.ordering.dto.QueueEnum;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashSaleOrderServiceImpl implements FlashSaleOrderService {

    private final StringRedisTemplate redisTemplate;
    private final AmqpTemplate amqpTemplate;
    private final MemberService memberService;
    private DefaultRedisScript<Long> stockScript;

    @PostConstruct
    public void init() {
        String lua =
                "local stock = tonumber(redis.call('get', KEYS[1])); "
                        + "if stock and stock >= tonumber(ARGV[1]) then "
                        + "  local bought = redis.call('sismember', KEYS[2], ARGV[2]); "
                        + "  if bought == 1 then return -1; end; "
                        + "  redis.call('decrby', KEYS[1], tonumber(ARGV[1])); "
                        + "  redis.call('sadd', KEYS[2], ARGV[2]); "
                        + "  return 1; "
                        + "else "
                        + "  return 0; "
                        + "end;";
        stockScript = new DefaultRedisScript<>();
        stockScript.setScriptText(lua);
        stockScript.setResultType(Long.class);
    }

    private boolean isRateLimited(Long memberId) {
        String key = "flash:rate_limit:" + memberId;
        String script =
                "local c = redis.call('incr', KEYS[1]); if c == 1 then redis.call('expire', KEYS[1], 1) end; return c";
        Long current =
                redisTemplate.execute(
                        new DefaultRedisScript<>(script, Long.class),
                        Collections.singletonList(key));
        return current != null && current > 1;
    }

    @Override
    public FlashOrderResponse generateFlashOrder(
            Long productId,
            Long flashPromotionId,
            Long flashPromotionSessionId,
            Integer quantity) {
        Member currentMember = memberService.getCurrentMember();
        if (currentMember == null) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }

        if (isRateLimited(currentMember.getId())) {
            Asserts.fail("Request too frequent, please try again later.");
        }

        String stockKey =
                "flash:stock:" + flashPromotionId + ":" + flashPromotionSessionId + ":" + productId;
        String userSetKey =
                "flash:users:" + flashPromotionId + ":" + flashPromotionSessionId + ":" + productId;
        Long result =
                redisTemplate.execute(
                        stockScript,
                        Arrays.asList(stockKey, userSetKey),
                        String.valueOf(quantity),
                        String.valueOf(currentMember.getId()));

        if (result == null || result == 0L) {
            Asserts.fail("Flash sale inventory is sold out!");
        } else if (result == -1L) {
            Asserts.fail("You have already purchased this flash sale item!");
        }

        FlashSaleOrderMessage message =
                new FlashSaleOrderMessage(
                        currentMember.getId(),
                        productId,
                        flashPromotionId,
                        flashPromotionSessionId,
                        quantity);

        amqpTemplate.convertAndSend(
                QueueEnum.QUEUE_FLASH_ORDER.getExchange(),
                QueueEnum.QUEUE_FLASH_ORDER.getRouteKey(),
                message);
        log.info(
                "Flash order message sent to MQ for member: {}, product: {}",
                currentMember.getId(),
                productId);

        return new FlashOrderResponse("Order request received, processing in background.");
    }
}
