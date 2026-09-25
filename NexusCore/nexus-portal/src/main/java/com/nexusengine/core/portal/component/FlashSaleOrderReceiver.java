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

    @org.springframework.beans.factory.annotation.Autowired
    private com.nexusengine.core.repository.SmsFlashPromotionProductRelationRepository flashPromotionProductRelationRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private com.nexusengine.core.repository.SmsFlashPromotionLogRepository flashPromotionLogRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private com.nexusengine.core.repository.OmsOrderItemRepository orderItemRepository;

    @RabbitHandler
    @org.springframework.transaction.annotation.Transactional
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
            // (a) Validate DB stock & deduct
            com.nexusengine.core.model.SmsFlashPromotionProductRelation relation = flashPromotionProductRelationRepository
                .findByFlashPromotionIdAndFlashPromotionSessionIdAndProductId(
                    message.getFlashPromotionId(), message.getFlashPromotionSessionId(), message.getProductId()
                ).orElseThrow(() -> new RuntimeException("Flash promotion relation not found"));

            if (relation.getFlashPromotionCount() < message.getQuantity()) {
                throw new RuntimeException("Insufficient flash stock in DB");
            }
            relation.setFlashPromotionCount(relation.getFlashPromotionCount() - message.getQuantity());
            flashPromotionProductRelationRepository.save(relation);

            // (b) Create OmsOrder + OmsOrderItem
            com.nexusengine.core.model.OmsOrder order = new com.nexusengine.core.model.OmsOrder();
            order.setMemberId(message.getMemberId());
            order.setCreateTime(new java.util.Date());
            order.setStatus(0);
            order.setDeleteStatus(0);
            order.setOrderType(1); // 1 = flash sale
            orderRepository.save(order);
            
            com.nexusengine.core.model.OmsOrderItem orderItem = new com.nexusengine.core.model.OmsOrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(message.getProductId());
            orderItem.setProductQuantity(message.getQuantity());
            orderItem.setProductPrice(relation.getFlashPromotionPrice());
            orderItemRepository.save(orderItem);

            // (c) log to sms_flash_promotion_log
            com.nexusengine.core.model.SmsFlashPromotionLog logEntry = new com.nexusengine.core.model.SmsFlashPromotionLog();
            logEntry.setMemberId(message.getMemberId());
            logEntry.setProductId(message.getProductId());
            logEntry.setFlashPromotionId(message.getFlashPromotionId());
            logEntry.setSessionId(message.getFlashPromotionSessionId());
            logEntry.setOrderId(order.getId());
            logEntry.setQuantity(message.getQuantity());
            logEntry.setStatus("SUCCESS");
            logEntry.setCreateTime(new java.util.Date());
            flashPromotionLogRepository.save(logEntry);

            log.info("Successfully persisted flash order to database for member: {}", message.getMemberId());
        } catch (Exception e) {
            log.error("Failed to process flash order for member: {}", message.getMemberId(), e);
            stringRedisTemplate.delete(idempotencyKey); // allow retry
            
            // Log failure
            com.nexusengine.core.model.SmsFlashPromotionLog logEntry = new com.nexusengine.core.model.SmsFlashPromotionLog();
            logEntry.setMemberId(message.getMemberId());
            logEntry.setProductId(message.getProductId());
            logEntry.setFlashPromotionId(message.getFlashPromotionId());
            logEntry.setSessionId(message.getFlashPromotionSessionId());
            logEntry.setQuantity(message.getQuantity());
            logEntry.setStatus("FAILED");
            logEntry.setCreateTime(new java.util.Date());
            flashPromotionLogRepository.save(logEntry);
            
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
