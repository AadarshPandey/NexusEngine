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

    // Normally we would inject OmsPortalOrderService here to actually write the order to Postgres.
    // @Autowired
    // private OmsPortalOrderService portalOrderService;

    @RabbitHandler
    public void handle(FlashSaleOrderMessage message) {
        log.info("Received flash order message for member: {}, product: {}", message.getMemberId(), message.getProductId());
        try {
            // Simulated delay for DB write
            Thread.sleep(50);
            
            // Generate standard OrderParam and call OmsPortalOrderService to insert into DB
            // OrderParam param = new OrderParam();
            // ... map message data to param ...
            // portalOrderService.generateOrder(param);
            
            log.info("Successfully persisted flash order to database for member: {}", message.getMemberId());
        } catch (Exception e) {
            log.error("Failed to process flash order for member: {}", message.getMemberId(), e);
            // In a real scenario, if DB insert fails, we would decrement the stock back in Redis here
        }
    }
}
