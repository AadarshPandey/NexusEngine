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
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OutboxEventProcessor.class);

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Autowired
    private org.redisson.api.RedissonClient redissonClient;

    @Scheduled(fixedDelay = 5000) // Run every 5 seconds
    @Transactional
    public void processOutboxEvents() {
        org.redisson.api.RLock lock = redissonClient.getLock("outbox_processor_lock");
        try {
            if (lock.tryLock(0, 5, java.util.concurrent.TimeUnit.SECONDS)) {
                List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatusOrderByCreatedAtAsc("PENDING", org.springframework.data.domain.PageRequest.of(0, 50));
                
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
                    } catch (NumberFormatException e) {
                        log.error("Invalid outbox event data for event {}: {}", event.getId(), e.getMessage());
                        event.setStatus("FAILED");
                        outboxEventRepository.save(event);
                    } catch (org.springframework.amqp.AmqpException e) {
                        log.error("Failed to process outbox event {}: {}", event.getId(), e.getMessage(), e);
                        event.setRetryCount(event.getRetryCount() != null ? event.getRetryCount() + 1 : 1);
                        if (event.getRetryCount() >= 3) {
                            event.setStatus("FAILED");
                        }
                        outboxEventRepository.save(event);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
