package com.nexusengine.core.portal.component;

import com.nexusengine.core.model.OutboxEvent;
import com.nexusengine.core.repository.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxEventProcessor {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Scheduled(fixedDelay = 5000) // Run every 5 seconds
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatusOrderByCreatedAtAsc("PENDING");
        
        for (OutboxEvent event : pendingEvents) {
            try {
                if ("CancelOrder".equals(event.getType())) {
                    Long orderId = Long.parseLong(event.getAggregateId());
                    Long delayTimes = Long.parseLong(event.getPayload());
                    
                    // Actually push to RabbitMQ now
                    cancelOrderSender.sendMessage(orderId, delayTimes);
                    
                    // Mark as sent
                    event.setStatus("SENT");
                    outboxEventRepository.save(event);
                }
            } catch (Exception e) {
                // If RabbitMQ fails, it will remain PENDING and retry on the next schedule
                // Alternatively, increment a retry count and mark FAILED if max retries exceeded
                // For FAANG basic implementation, we just catch and leave as PENDING
            }
        }
    }
}
