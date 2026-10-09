package com.nexusengine.core.portal.integration;

import com.nexusengine.core.model.StagedEvent;
import com.nexusengine.core.portal.component.StagedEventProcessor;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.repository.StagedEventRepository;
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
public class StagedEventIntegrationTest {

    private final StagedEventRepository stagedEventRepository;
    private final StagedEventProcessor stagedEventProcessor;

    @Autowired
    StagedEventIntegrationTest(StagedEventRepository stagedEventRepository, StagedEventProcessor stagedEventProcessor) {
        this.stagedEventRepository = stagedEventRepository;
        this.stagedEventProcessor = stagedEventProcessor;
    }




    @Test
    void stagedEvent_PendingToSent_PublishesToRabbitMQ() {
        StagedEvent event = new StagedEvent();
        event.setType("CancelOrder");
        event.setAggregateType("Order");
        event.setAggregateId("9999");
        event.setPayload("60000");
        event.setStatus("PENDING");
        event.setRetryCount(0);
        event.setCreatedAt(OffsetDateTime.now());
        
        event = stagedEventRepository.save(event);
        Long eventId = event.getId();

        // Processor is scheduled, but we can call it manually for the test
        stagedEventProcessor.processStagedEvents();

        await().atMost(10, SECONDS).untilAsserted(() -> {
            StagedEvent processedEvent = stagedEventRepository.findById(eventId).orElseThrow();
            assertThat(processedEvent.getStatus()).isEqualTo("SENT");
        });
    }
}
