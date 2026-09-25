package com.nexusengine.core.portal.unit.component;

import com.nexusengine.core.model.OutboxEvent;
import com.nexusengine.core.portal.component.CancelOrderSender;
import com.nexusengine.core.portal.component.OutboxEventProcessor;
import com.nexusengine.core.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.AmqpException;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutboxEventProcessorTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;
    @Mock
    private CancelOrderSender cancelOrderSender;
    @Mock
    private RedissonClient redissonClient;

    @InjectMocks
    private OutboxEventProcessor processor;

    private OutboxEvent event;
    private RLock mockLock;

    @BeforeEach
    void setUp() throws InterruptedException {
        event = new OutboxEvent();
        event.setId(1L);
        event.setType("CancelOrder");
        event.setAggregateId("100");
        event.setPayload("60000");
        event.setStatus("PENDING");
        event.setRetryCount(0);

        mockLock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(mockLock);
    }

    @Test
    void processOutboxEvents_PendingEvents_SendsToRabbitAndMarksSent() throws InterruptedException {
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(mockLock.isHeldByCurrentThread()).thenReturn(true);
        when(outboxEventRepository.findByStatusOrderByCreatedAtAsc(eq("PENDING"), any()))
                .thenReturn(Collections.singletonList(event));

        processor.processOutboxEvents();

        verify(cancelOrderSender).sendMessage(100L, 60000L);
        assertEquals("SENT", event.getStatus());
        verify(outboxEventRepository).save(event);
    }

    @Test
    void processOutboxEvents_AmqpException_IncrementsRetryCount() throws InterruptedException {
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(mockLock.isHeldByCurrentThread()).thenReturn(true);
        when(outboxEventRepository.findByStatusOrderByCreatedAtAsc(eq("PENDING"), any()))
                .thenReturn(Collections.singletonList(event));
        
        doThrow(new AmqpException("RabbitMQ down")).when(cancelOrderSender).sendMessage(anyLong(), anyLong());

        processor.processOutboxEvents();

        assertEquals(1, event.getRetryCount());
        assertEquals("PENDING", event.getStatus()); // still pending
        verify(outboxEventRepository).save(event);
    }

    @Test
    void processOutboxEvents_ExceedsMaxRetries_MarksAsFailed() throws InterruptedException {
        event.setRetryCount(2); // this will be the 3rd retry
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(mockLock.isHeldByCurrentThread()).thenReturn(true);
        when(outboxEventRepository.findByStatusOrderByCreatedAtAsc(eq("PENDING"), any()))
                .thenReturn(Collections.singletonList(event));
        
        doThrow(new AmqpException("RabbitMQ down")).when(cancelOrderSender).sendMessage(anyLong(), anyLong());

        processor.processOutboxEvents();

        assertEquals(3, event.getRetryCount());
        assertEquals("FAILED", event.getStatus());
        verify(outboxEventRepository).save(event);
    }

    @Test
    void processOutboxEvents_InvalidPayload_MarksAsFailed() throws InterruptedException {
        event.setAggregateId("invalid");
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(mockLock.isHeldByCurrentThread()).thenReturn(true);
        when(outboxEventRepository.findByStatusOrderByCreatedAtAsc(eq("PENDING"), any()))
                .thenReturn(Collections.singletonList(event));

        processor.processOutboxEvents();

        assertEquals("FAILED", event.getStatus());
        verify(outboxEventRepository).save(event);
        verify(cancelOrderSender, never()).sendMessage(anyLong(), anyLong());
    }

    @Test
    void processOutboxEvents_LockNotAcquired_SkipsProcessing() throws InterruptedException {
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        processor.processOutboxEvents();

        verify(outboxEventRepository, never()).findByStatusOrderByCreatedAtAsc(anyString(), any());
    }
}
