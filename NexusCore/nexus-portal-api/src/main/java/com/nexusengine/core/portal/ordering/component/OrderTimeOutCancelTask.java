package com.nexusengine.core.portal.ordering.component;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.nexusengine.core.portal.ordering.service.OrderService;


/**
 * Represents the OrderTimeOutCancelTask component.
 * Provides core functionality and operations for OrderTimeOutCancelTask.
 */
//@Component
public class OrderTimeOutCancelTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTimeOutCancelTask.class);
    private final OrderService portalOrderService;

    public OrderTimeOutCancelTask(OrderService portalOrderService) {
        this.portalOrderService = portalOrderService;
    }

        /**
     * Executes the operation.
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void cancelTimeOutOrder(){
        Integer count = portalOrderService.cancelTimeOutOrder();
        LOGGER.info("Success",count);
    }
}
