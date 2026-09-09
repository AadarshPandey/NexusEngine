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

    @RabbitHandler
    public void handle(FlashSaleOrderMessage message) {
        log.info("Received flash order message for member: {}, product: {}", message.getMemberId(), message.getProductId());
        try {
            // Simulated delay for DB write
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
            String stockKey = "flash:stock:" + message.getFlashPromotionId() + ":" + message.getFlashPromotionSessionId() + ":" + message.getProductId();
            redisService.incr(stockKey, message.getQuantity());
            log.info("Compensated Redis stock for key: {}, quantity: {}", stockKey, message.getQuantity());
        }
    }
}
