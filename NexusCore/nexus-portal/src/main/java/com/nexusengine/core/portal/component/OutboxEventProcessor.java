package com.nexusengine.core.portal.component;

import com.nexusengine.core.model.OutboxEvent;
import com.nexusengine.core.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.amqp.AmqpException;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OutboxEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(OutboxEventProcessor.class);

    private final OutboxEventRepository outboxEventRepository;
    private final CancelOrderSender cancelOrderSender;
    private final RedissonClient redissonClient;

    public OutboxEventProcessor(OutboxEventRepository outboxEventRepository,
                                CancelOrderSender cancelOrderSender,
                                RedissonClient redissonClient) {
        this.outboxEventRepository = outboxEventRepository;
        this.cancelOrderSender = cancelOrderSender;
        this.redissonClient = redissonClient;
    }

    @Scheduled(fixedDelay = 5000) // Run every 5 seconds
    @Transactional
    public void processOutboxEvents() {
        RLock lock = redissonClient.getLock("outbox_processor_lock");
        try {
            if (lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatusOrderByCreatedAtAsc("PENDING", PageRequest.of(0, 50));
                
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
                    } catch (AmqpException e) {
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
