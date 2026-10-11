package com.nexusengine.core.portal.component;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.nexusengine.core.portal.ordering.service.OrderService;


/**
 * Represents the CancelOrderReceiver component.
 * Provides core functionality and operations for CancelOrderReceiver.
 */
@Component
@RabbitListener(queues = "nexus.order.cancel")
public class CancelOrderReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);
    private final OrderService portalOrderService;

    public CancelOrderReceiver(OrderService portalOrderService) {
        this.portalOrderService = portalOrderService;
    }

    @RabbitHandler
    public void handle(Long orderId){
        try {
            portalOrderService.cancelOrder(orderId);
            LOGGER.info("process orderId:{}",orderId);
        } catch (Exception e) {
            LOGGER.error("Failed to process cancellation for order {}: {}", orderId, e.getMessage());
            throw new org.springframework.amqp.AmqpRejectAndDontRequeueException("Failed to cancel order", e);
        }
    }
}
