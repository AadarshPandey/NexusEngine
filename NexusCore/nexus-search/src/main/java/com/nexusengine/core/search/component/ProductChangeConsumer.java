package com.nexusengine.core.search.component;

import com.nexusengine.core.search.service.EsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ consumer that listens for product change events and updates Elasticsearch in real-time
 */
@Component
public class ProductChangeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductChangeConsumer.class);

    @Autowired
    private EsProductService esProductService;

    @RabbitListener(queues = "nexuscore.product.sync.queue")
    public void handleProductChange(String message) {
        LOGGER.info("Received product sync event: {}", message);
        try {
            String[] parts = message.split(":");
            Long productId = Long.parseLong(parts[0]);
            String action = parts[1];

            switch (action) {
                case "create":
                case "update":
                    esProductService.create(productId);
                    LOGGER.info("Product {} indexed in Elasticsearch", productId);
                    break;
                case "delete":
                    esProductService.delete(productId);
                    LOGGER.info("Product {} removed from Elasticsearch", productId);
                    break;
                default:
                    LOGGER.warn("Unknown action: {}", action);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to process product sync event: {}", e.getMessage());
        }
    }
}
