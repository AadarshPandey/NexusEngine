package com.nexusengine.core.config;

import com.nexusengine.core.component.ProductSyncSender;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
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
