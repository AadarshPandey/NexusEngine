package com.nexusengine.core.portal.integration;

import com.nexusengine.core.model.OutboxEvent;
import com.nexusengine.core.portal.component.OutboxEventProcessor;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.repository.OutboxEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
@Testcontainers
public class OutboxEventIntegrationTest {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private OutboxEventProcessor outboxEventProcessor;

    @Test
    void outboxEvent_PendingToSent_PublishesToRabbitMQ() {
        OutboxEvent event = new OutboxEvent();
        event.setType("CancelOrder");
        event.setAggregateType("Order");
        event.setAggregateId("9999");
        event.setPayload("60000");
        event.setStatus("PENDING");
        event.setRetryCount(0);
        event.setCreatedAt(new Date());
        
        event = outboxEventRepository.save(event);
        Long eventId = event.getId();

        // Processor is scheduled, but we can call it manually for the test
        outboxEventProcessor.processOutboxEvents();

        await().atMost(10, SECONDS).untilAsserted(() -> {
            OutboxEvent processedEvent = outboxEventRepository.findById(eventId).orElseThrow();
            assertThat(processedEvent.getStatus()).isEqualTo("SENT");
        });
    }
}
