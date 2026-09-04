package com.nexusengine.core.config;

import com.nexusengine.core.component.ProductSyncSender;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for product sync events
 */
@Configuration
public class AdminRabbitMqConfig {

    @Bean
    public DirectExchange productSyncExchange() {
        return new DirectExchange(ProductSyncSender.EXCHANGE_NAME);
    }

    @Bean
    public Queue productSyncQueue() {
        return new Queue("nexuscore.product.sync.queue", true);
    }

    @Bean
    public Binding productSyncBinding(DirectExchange productSyncExchange, Queue productSyncQueue) {
        return BindingBuilder.bind(productSyncQueue)
                .to(productSyncExchange)
                .with(ProductSyncSender.ROUTING_KEY);
    }
}
