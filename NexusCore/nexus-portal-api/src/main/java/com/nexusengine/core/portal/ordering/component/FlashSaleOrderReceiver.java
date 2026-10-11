package com.nexusengine.core.portal.ordering.component;

import com.nexusengine.core.domain.ordering.entity.OrderLine;

import com.nexusengine.core.domain.promotion.entity.FlashSaleItem;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.promotion.repository.FlashSaleItemRepository;
import com.nexusengine.core.domain.promotion.entity.FlashSalePurchase;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.promotion.repository.FlashSalePurchaseRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.nexusengine.core.portal.ordering.dto.FlashSaleOrderMessage;


@Slf4j
@Component
@RabbitListener(queues = "nexus.flash.order")
public class FlashSaleOrderReceiver {

    @org.springframework.beans.factory.annotation.Autowired
    private CustomerOrderRepository orderRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.nexusengine.core.common.service.RedisService redisService;

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate;

    @org.springframework.beans.factory.annotation.Autowired
    private FlashSaleItemRepository flashPromotionProductRelationRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private FlashSalePurchaseRepository flashPromotionLogRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private OrderLineRepository orderItemRepository;

    @RabbitHandler
    @org.springframework.transaction.annotation.Transactional
    public void handle(FlashSaleOrderMessage message) {
        log.info("Received flash order message for member: {}, product: {}", message.memberId(), message.productId());

        // Fix 3: Idempotency check
        String idempotencyKey = "flash:order_processed:" + message.flashPromotionSessionId() + ":" + message.productId() + ":" + message.memberId();
        Boolean isFirstProcess = stringRedisTemplate.opsForValue().setIfAbsent(idempotencyKey, "1", java.time.Duration.ofHours(24));
        if (Boolean.FALSE.equals(isFirstProcess)) {
            log.info("Message already processed for member {}, ignoring.", message.memberId());
            return;
        }

        try {
            // (a) Validate DB stock & deduct
            FlashSaleItem relation = flashPromotionProductRelationRepository
                .findByFlashPromotionIdAndFlashPromotionSessionIdAndProductId(
                    message.flashPromotionId(), message.flashPromotionSessionId(), message.productId()
                ).orElseThrow(() -> new RuntimeException("Flash promotion relation not found"));

            if (relation.getFlashPromotionCount() < message.quantity()) {
                throw new RuntimeException("Insufficient flash stock in DB");
            }
            relation.setFlashPromotionCount(relation.getFlashPromotionCount() - message.quantity());
            flashPromotionProductRelationRepository.save(relation);

            // (b) Create CustomerOrder + OrderLine
            CustomerOrder order = new CustomerOrder();
            order.setMemberId(message.memberId());
            order.setCreateTime(java.time.OffsetDateTime.now());
            order.setStatus(0);
            order.setDeleteStatus(0);
            order.setOrderType(1); // 1 = flash sale
            orderRepository.save(order);

            OrderLine orderItem = new OrderLine();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(message.productId());
            orderItem.setProductQuantity(message.quantity());
            orderItem.setProductPrice(relation.getFlashPromotionPrice());
            orderItemRepository.save(orderItem);

            // (c) log to sms_flash_promotion_log
            FlashSalePurchase logEntry = new FlashSalePurchase();
            logEntry.setMemberId(message.memberId());
            logEntry.setProductId(message.productId());
            logEntry.setFlashPromotionId(message.flashPromotionId());
            logEntry.setSessionId(message.flashPromotionSessionId());
            logEntry.setOrderId(order.getId());
            logEntry.setQuantity(message.quantity());
            logEntry.setStatus("SUCCESS");
            logEntry.setCreateTime(java.time.OffsetDateTime.now());
            flashPromotionLogRepository.save(logEntry);

            log.info("Successfully persisted flash order to database for member: {}", message.memberId());
        } catch (Exception e) {
            log.error("Failed to process flash order for member: {}", message.memberId(), e);
            stringRedisTemplate.delete(idempotencyKey); // allow retry

            // Log failure
            FlashSalePurchase logEntry = new FlashSalePurchase();
            logEntry.setMemberId(message.memberId());
            logEntry.setProductId(message.productId());
            logEntry.setFlashPromotionId(message.flashPromotionId());
            logEntry.setSessionId(message.flashPromotionSessionId());
            logEntry.setQuantity(message.quantity());
            logEntry.setStatus("FAILED");
            logEntry.setCreateTime(java.time.OffsetDateTime.now());
            flashPromotionLogRepository.save(logEntry);

            try {
                // Fix 7: Safe compensation
                String stockKey = "flash:stock:" + message.flashPromotionId() + ":" + message.flashPromotionSessionId() + ":" + message.productId();
                redisService.incr(stockKey, message.quantity());

                String userSetKey = "flash:users:" + message.flashPromotionId() + ":" + message.flashPromotionSessionId() + ":" + message.productId();
                stringRedisTemplate.opsForSet().remove(userSetKey, String.valueOf(message.memberId()));
                log.info("Compensated Redis stock for key: {}, quantity: {}", stockKey, message.quantity());
            } catch (Exception redisEx) {
                log.error("CRITICAL: Failed to compensate Redis stock!", redisEx);
                // In production, send to Dead Letter Queue (DLQ) or fallback DB table here
            }
        }
    }
}
