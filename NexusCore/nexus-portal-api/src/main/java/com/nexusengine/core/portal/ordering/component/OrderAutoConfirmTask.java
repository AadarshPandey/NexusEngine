package com.nexusengine.core.portal.ordering.component;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.nexusengine.core.portal.ordering.service.OrderService;


/**
 * Periodically auto-confirms receipt for orders shipped longer than the policy days.
 */
@org.springframework.stereotype.Component
public class OrderAutoConfirmTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAutoConfirmTask.class);
    private final OrderService portalOrderService;

    public OrderAutoConfirmTask(OrderService portalOrderService) {
        this.portalOrderService = portalOrderService;
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
