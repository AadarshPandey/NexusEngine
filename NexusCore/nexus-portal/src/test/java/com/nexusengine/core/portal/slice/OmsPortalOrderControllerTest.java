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

    @Mock
    private com.nexusengine.core.portal.service.UmsMemberService memberService;

    @Mock
    private com.nexusengine.core.repository.OmsOrderRepository omsOrderRepository;

    @InjectMocks
    private OmsPortalOrderController orderController;

    private final String orderId = "order_12345";
    private final String paymentId = "pay_67890";
    private final String testSignature = "test_signature";

    @Test
    void verifyRazorpayPayment_ValidSignature_CallsPaySuccess() throws Exception {
        com.nexusengine.core.portal.domain.OmsOrderDetail detail = new com.nexusengine.core.portal.domain.OmsOrderDetail();
        detail.setMemberId(1L);
        detail.setStatus(0);
        detail.setPayAmount(new java.math.BigDecimal("100.00"));
        when(portalOrderService.detail(100L)).thenReturn(detail);
        
        com.nexusengine.core.model.UmsMember member = new com.nexusengine.core.model.UmsMember();
        member.setId(1L);
        when(memberService.getCurrentMember()).thenReturn(member);
        
        when(omsOrderRepository.existsByPaymentId(paymentId)).thenReturn(false);
        when(razorpayPaymentGatewayService.verifySignature(orderId, paymentId, testSignature)).thenReturn(true);
        when(razorpayPaymentGatewayService.verifyPaymentAmount(paymentId, 10000)).thenReturn(true);

        CommonResult result = orderController.verifyRazorpayPayment(100L, paymentId, orderId, testSignature);

        assertEquals(200, result.getCode());
        assertEquals("Payment successful", result.getData());
        verify(portalOrderService).paySuccess(100L, 2);
    }

    @Test
    void verifyRazorpayPayment_InvalidSignature_ReturnsFailed() {
        com.nexusengine.core.portal.domain.OmsOrderDetail detail = new com.nexusengine.core.portal.domain.OmsOrderDetail();
        detail.setMemberId(1L);
        detail.setStatus(0);
        when(portalOrderService.detail(100L)).thenReturn(detail);
        
        com.nexusengine.core.model.UmsMember member = new com.nexusengine.core.model.UmsMember();
        member.setId(1L);
        when(memberService.getCurrentMember()).thenReturn(member);
        
        when(omsOrderRepository.existsByPaymentId(paymentId)).thenReturn(false);
        when(razorpayPaymentGatewayService.verifySignature(orderId, paymentId, "invalid_sig")).thenReturn(false);

        CommonResult result = orderController.verifyRazorpayPayment(100L, paymentId, orderId, "invalid_sig");

        assertEquals(500, result.getCode());
        assertEquals("Invalid signature", result.getMessage());
        verify(portalOrderService, never()).paySuccess(anyLong(), anyInt());
    }
}
