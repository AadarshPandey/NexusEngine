package com.nexusengine.core.portal.ordering.controller;

import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.service.OrderService;
import com.nexusengine.core.portal.payment.service.PaymentGatewayService;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerWebhookUnitTest {

    private OrderService orderService;
    private PaymentGatewayService paymentGatewayService;
    private MemberService memberService;
    private CustomerOrderRepository customerOrderRepository;
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        paymentGatewayService = mock(PaymentGatewayService.class);
        memberService = mock(MemberService.class);
        customerOrderRepository = mock(CustomerOrderRepository.class);

        orderController = new OrderController(orderService, memberService, paymentGatewayService, customerOrderRepository);
    }

    @Test
    void handlePaymentWebhook_MissingSignature_Returns401() {
        ResponseEntity<String> response = orderController.handlePaymentWebhook(null, "{\"event\":\"payment.captured\"}");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Missing X-Razorpay-Signature header", response.getBody());
        verifyNoInteractions(orderService);
    }

    @Test
    void handlePaymentWebhook_BlankSignature_Returns401() {
        ResponseEntity<String> response = orderController.handlePaymentWebhook("  ", "{\"event\":\"payment.captured\"}");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Missing X-Razorpay-Signature header", response.getBody());
        verifyNoInteractions(orderService);
    }

    @Test
    void handlePaymentWebhook_InvalidSignature_Returns401() {
        String payload = "{\"event\":\"payment.captured\"}";
        String signature = "invalid_sig";
        when(paymentGatewayService.verifyWebhookSignature(payload, signature)).thenReturn(false);

        ResponseEntity<String> response = orderController.handlePaymentWebhook(signature, payload);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid webhook signature", response.getBody());
        verifyNoInteractions(orderService);
    }

    @Test
    void handlePaymentWebhook_ValidSignature_ProcessesSuccessfully() {
        String payload = "{\"event\":\"payment.captured\"}";
        String signature = "valid_sig";
        when(paymentGatewayService.verifyWebhookSignature(payload, signature)).thenReturn(true);

        ResponseEntity<String> response = orderController.handlePaymentWebhook(signature, payload);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Webhook processed successfully", response.getBody());
        verify(orderService).handlePaymentWebhook(payload, signature);
    }

    @Test
    void handlePaymentWebhook_ServiceThrowsException_Returns500ForRetry() {
        String payload = "{\"event\":\"payment.captured\"}";
        String signature = "valid_sig";
        when(paymentGatewayService.verifyWebhookSignature(payload, signature)).thenReturn(true);
        doThrow(new RuntimeException("DB Connection timeout")).when(orderService).handlePaymentWebhook(payload, signature);

        ResponseEntity<String> response = orderController.handlePaymentWebhook(signature, payload);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Webhook processing failed", response.getBody());
    }
}
