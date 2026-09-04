package com.nexusengine.core.portal.slice;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.portal.controller.OmsPortalOrderController;
import com.nexusengine.core.portal.service.OmsPortalOrderService;
import com.nexusengine.core.portal.service.RazorpayPaymentGatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmsPortalOrderControllerTest {

    @Mock
    private OmsPortalOrderService portalOrderService;

    @Mock
    private RazorpayPaymentGatewayService razorpayPaymentGatewayService;

    @InjectMocks
    private OmsPortalOrderController orderController;

    private final String orderId = "order_12345";
    private final String paymentId = "pay_67890";
    private final String testSignature = "test_signature";

    @Test
    void verifyRazorpayPayment_ValidSignature_CallsPaySuccess() throws Exception {
        when(razorpayPaymentGatewayService.verifySignature(orderId, paymentId, testSignature)).thenReturn(true);

        CommonResult result = orderController.verifyRazorpayPayment(100L, paymentId, orderId, testSignature);

        assertEquals(200, result.getCode());
        assertEquals("Payment successful", result.getData());
        verify(portalOrderService).paySuccess(100L, 2);
    }

    @Test
    void verifyRazorpayPayment_InvalidSignature_ReturnsFailed() {
        when(razorpayPaymentGatewayService.verifySignature(orderId, paymentId, "invalid_sig")).thenReturn(false);

        CommonResult result = orderController.verifyRazorpayPayment(100L, paymentId, orderId, "invalid_sig");

        assertEquals(500, result.getCode());
        assertEquals("Invalid signature", result.getMessage());
        verify(portalOrderService, never()).paySuccess(anyLong(), anyInt());
    }
}
