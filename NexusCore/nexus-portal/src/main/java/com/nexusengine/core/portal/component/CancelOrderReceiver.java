package com.nexusengine.core.portal.component;

import com.nexusengine.core.portal.service.OmsPortalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  Auto-generated documentation
 * Created by macro on 2018/9/14.
 */
@Component
@RabbitListener(queues = "nexus.order.cancel")
public class CancelOrderReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);
    @Autowired
    private OmsPortalOrderService portalOrderService;
    @RabbitHandler
    public void handle(Long orderId){
        portalOrderService.cancelOrder(orderId);
        LOGGER.info("process orderId:{}",orderId);
    }
}
