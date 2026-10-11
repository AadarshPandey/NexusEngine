package com.nexusengine.core.portal.ordering.component;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.nexusengine.core.portal.ordering.service.OrderService;


/**
 * Represents the OrderTimeOutCancelTask component.
 * Provides core functionality and operations for OrderTimeOutCancelTask.
 */
@org.springframework.stereotype.Component
public class OrderTimeOutCancelTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTimeOutCancelTask.class);
    private final OrderService portalOrderService;

    public OrderTimeOutCancelTask(OrderService portalOrderService) {
        this.portalOrderService = portalOrderService;
    }

    /**
     * Periodically cancels unpaid orders that have exceeded the payment timeout.
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    public void cancelTimeOutOrder() {
        Integer count = portalOrderService.cancelTimeOutOrder();
        LOGGER.info("Cancelled {} timed out orders", count);
    }

    /**
     * Periodically auto-confirms receipt for orders shipped longer than the policy days.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void autoConfirmShippedOrders() {
        int count = portalOrderService.autoConfirmShippedOrders();
        LOGGER.info("Auto-confirmed receipt for {} shipped orders", count);
    }
}
