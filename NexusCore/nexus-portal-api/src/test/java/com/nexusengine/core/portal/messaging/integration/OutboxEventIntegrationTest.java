package com.nexusengine.core.portal.messaging.integration;

import com.nexusengine.core.domain.messaging.entity.OutboxEvent;
import com.nexusengine.core.portal.messaging.component.OutboxEventProcessor;
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
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.catalog.controller.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.ordering.controller.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.customer.controller.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.promotion.controller.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.ai.controller.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.home.controller.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;
import com.nexusengine.core.portal.engagement.controller.*;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.domain.messaging.repository.OutboxEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
@Testcontainers
public class OutboxEventIntegrationTest {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor stagedEventProcessor;

    @Autowired
    OutboxEventIntegrationTest(OutboxEventRepository outboxEventRepository, OutboxEventProcessor stagedEventProcessor) {
        this.outboxEventRepository = outboxEventRepository;
        this.stagedEventProcessor = stagedEventProcessor;
    }




    @Test
    void stagedEvent_PendingToSent_PublishesToRabbitMQ() {
        OutboxEvent event = new OutboxEvent();
        event.setType("CancelOrder");
        event.setAggregateType("Order");
        event.setAggregateId("9999");
        event.setPayload("60000");
        event.setStatus("PENDING");
        event.setRetryCount(0);
        event.setCreatedAt(OffsetDateTime.now());
        
        event = outboxEventRepository.save(event);
        Long eventId = event.getId();

        // Processor is scheduled, but we can call it manually for the test
        stagedEventProcessor.processStagedEvents();

        await().atMost(10, SECONDS).untilAsserted(() -> {
            OutboxEvent processedEvent = outboxEventRepository.findById(eventId).orElseThrow();
            assertThat(processedEvent.getStatus()).isEqualTo("SENT");
        });
    }
}
