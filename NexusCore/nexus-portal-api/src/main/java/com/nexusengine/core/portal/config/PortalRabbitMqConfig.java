package com.nexusengine.core.portal.config;


import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Represents the PortalRabbitMqConfig component.
 * Provides core functionality and operations for PortalRabbitMqConfig.
 */
@Configuration
public class PortalRabbitMqConfig {

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    @Bean
    DirectExchange orderDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    @Bean
    DirectExchange orderTtlDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getName());
    }

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getName())
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey())
                .build();
    }

        /**
     * Executes the operation.
     * @param orderDirect the orderDirect
     * @param orderQueue the orderQueue
     * @return the result of the operation
     */
    @Bean
    Binding orderBinding(DirectExchange orderDirect,Queue orderQueue){
        return BindingBuilder
                .bind(orderQueue)
                .to(orderDirect)
                .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }

        /**
     * Executes the operation.
     * @param orderTtlDirect the orderTtlDirect
     * @param orderTtlQueue the orderTtlQueue
     * @return the result of the operation
     */
    @Bean
    Binding orderTtlBinding(DirectExchange orderTtlDirect,Queue orderTtlQueue){
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlDirect)
                .with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey());
    }

    @Bean
    DirectExchange flashOrderDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_FLASH_ORDER.getExchange())
                .durable(true)
                .build();
    }

    @Bean
    public Queue flashOrderQueue() {
        return new Queue(QueueEnum.QUEUE_FLASH_ORDER.getName());
    }

    @Bean
    Binding flashOrderBinding(DirectExchange flashOrderDirect, Queue flashOrderQueue){
        return BindingBuilder
                .bind(flashOrderQueue)
                .to(flashOrderDirect)
                .with(QueueEnum.QUEUE_FLASH_ORDER.getRouteKey());
    }

}
