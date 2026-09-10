package com.nexusengine.core.portal.component;

import com.nexusengine.core.portal.domain.FlashSaleOrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "nexus.flash.order")
public class FlashSaleOrderReceiver {

    @org.springframework.beans.factory.annotation.Autowired
    private com.nexusengine.core.repository.OmsOrderRepository orderRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.nexusengine.core.common.service.RedisService redisService;

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void handle(FlashSaleOrderMessage message) {
        log.info("Received flash order message for member: {}, product: {}", message.getMemberId(), message.getProductId());
        
        // Fix 3: Idempotency check
        String idempotencyKey = "flash:order_processed:" + message.getFlashPromotionSessionId() + ":" + message.getProductId() + ":" + message.getMemberId();
        Boolean isFirstProcess = stringRedisTemplate.opsForValue().setIfAbsent(idempotencyKey, "1", java.time.Duration.ofHours(24));
        if (Boolean.FALSE.equals(isFirstProcess)) {
            log.info("Message already processed for member {}, ignoring.", message.getMemberId());
            return;
        }

        try {
            Thread.sleep(50);
            com.nexusengine.core.model.OmsOrder order = new com.nexusengine.core.model.OmsOrder();
            order.setMemberId(message.getMemberId());
            order.setCreateTime(new java.util.Date());
            order.setStatus(0);
            order.setDeleteStatus(0);
            order.setOrderType(1); // 1 = flash sale
            orderRepository.save(order);
            log.info("Successfully persisted flash order to database for member: {}", message.getMemberId());
        } catch (Exception e) {
            log.error("Failed to process flash order for member: {}", message.getMemberId(), e);
            stringRedisTemplate.delete(idempotencyKey); // allow retry
            try {
                // Fix 7: Safe compensation
                String stockKey = "flash:stock:" + message.getFlashPromotionId() + ":" + message.getFlashPromotionSessionId() + ":" + message.getProductId();
                redisService.incr(stockKey, message.getQuantity());
                
                String userSetKey = "flash:users:" + message.getFlashPromotionId() + ":" + message.getFlashPromotionSessionId() + ":" + message.getProductId();
                stringRedisTemplate.opsForSet().remove(userSetKey, String.valueOf(message.getMemberId()));
                log.info("Compensated Redis stock for key: {}, quantity: {}", stockKey, message.getQuantity());
            } catch (Exception redisEx) {
                log.error("CRITICAL: Failed to compensate Redis stock!", redisEx);
                // In production, send to Dead Letter Queue (DLQ) or fallback DB table here
            }
        }
    }
}
