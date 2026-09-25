package com.nexusengine.core.portal.unit.component;

import com.nexusengine.core.portal.component.CancelOrderReceiver;
import com.nexusengine.core.portal.service.OmsPortalOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelOrderReceiverTest {

    @Mock
    private OmsPortalOrderService portalOrderService;

    @InjectMocks
    private CancelOrderReceiver receiver;

    @Test
    void handle_ValidOrderId_CancelsOrder() {
        receiver.handle(100L);
        verify(portalOrderService).cancelOrder(100L);
    }

    @Test
    void handle_ServiceThrows_WrapsInAmqpRejectException() {
        doThrow(new RuntimeException("DB error")).when(portalOrderService).cancelOrder(100L);

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> {
            receiver.handle(100L);
        });
    }
}
