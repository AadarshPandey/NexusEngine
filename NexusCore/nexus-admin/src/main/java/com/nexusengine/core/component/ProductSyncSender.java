package com.nexusengine.core.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends product change events to RabbitMQ for real-time Elasticsearch sync
 */
@Component
public class ProductSyncSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSyncSender.class);

    public static final String EXCHANGE_NAME = "nexuscore.product.sync";
    public static final String ROUTING_KEY = "product.change";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Send a product change event
     * @param productId the product ID that was changed
     * @param action "create", "update", or "delete"
     */
    public void sendProductChangeEvent(Long productId, String action) {
        String message = productId + ":" + action;
        LOGGER.info("Sending product sync event: {}", message);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
