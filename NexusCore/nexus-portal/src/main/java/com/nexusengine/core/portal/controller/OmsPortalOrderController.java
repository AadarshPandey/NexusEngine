package com.nexusengine.core.portal.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.portal.domain.ConfirmOrderResult;
import com.nexusengine.core.portal.domain.OmsOrderDetail;
import com.nexusengine.core.portal.domain.OrderParam;
import com.nexusengine.core.portal.service.OmsPortalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Customer-facing order management controller.
 * Handles order creation, listing, cancellation, and Razorpay payment integration.
 */
@RestController
@Tag(name = "OmsPortalOrderController", description = "Oms portal order controller APIs")
@RequestMapping("/portal/order")
@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class OmsPortalOrderController {
    private final OmsPortalOrderService portalOrderService;
    private final com.nexusengine.core.portal.service.UmsMemberService memberService;

    @Operation(summary = "Generate confirm order Operation")
    @PostMapping("/generateConfirmOrder")

    public CommonResult<ConfirmOrderResult> generateConfirmOrder(@RequestBody List<Long> cartIds) {
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(cartIds);
        return CommonResult.success(confirmOrderResult);
    }

    @Operation(summary = "Generate order Operation")
    @PostMapping("/generateOrder")

    public CommonResult generateOrder(@RequestBody OrderParam orderParam) {
        Map<String, Object> result = portalOrderService.generateOrder(orderParam);
        return CommonResult.success(result, "Success");
    }


    @Operation(summary = "API Operation")
    @Parameter(name = "status", description = "Description",
            in = ParameterIn.QUERY, schema = @Schema(type = "integer",defaultValue = "-1",allowableValues = {"-1","0","1","2","3","4"}))
    @GetMapping("/list")

    public CommonResult<CommonPage<OmsOrderDetail>> list(@RequestParam Integer status,
                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        CommonPage<OmsOrderDetail> orderPage = portalOrderService.list(status,pageNum,pageSize);
        return CommonResult.success(orderPage);
    }

    @Operation(summary = "Detail Operation")
    @GetMapping("/detail/{orderId}")

    public CommonResult<OmsOrderDetail> detail(@PathVariable Long orderId) {
        OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
        return CommonResult.success(orderDetail);
    }

    @Operation(summary = "Cancel user order Operation")
    @PostMapping("/cancelUserOrder")

    public CommonResult cancelUserOrder(Long orderId) {
        portalOrderService.cancelOrder(orderId);
        return CommonResult.success(null);
    }

    @Operation(summary = "Confirm receive order Operation")
    @PostMapping("/confirmReceiveOrder")

    public CommonResult confirmReceiveOrder(Long orderId) {
        portalOrderService.confirmReceiveOrder(orderId);
        return CommonResult.success(null);
    }

    @Operation(summary = "Delete order Operation")
    @PostMapping("/deleteOrder")

    public CommonResult deleteOrder(Long orderId) {
        portalOrderService.deleteOrder(orderId);
        return CommonResult.success(null);
    }
    private final com.nexusengine.core.portal.service.RazorpayPaymentGatewayService razorpayPaymentGatewayService;

    @Operation(summary = "Create Razorpay Order")
    @PostMapping("/createRazorpayOrder")

    public CommonResult<Map<String, String>> createRazorpayOrder(@RequestParam Long orderId) {
        try {
            OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
            if (orderDetail == null) return CommonResult.failed("Order not found");
            
            int amount = orderDetail.getPayAmount().multiply(new java.math.BigDecimal("100")).intValue();
            Map<String, String> result = razorpayPaymentGatewayService.createOrder(amount, orderDetail.getOrderSn());
            
            return CommonResult.success(result);
        } catch (RuntimeException e) {
            log.error("Failed to create Razorpay order", e);
            if (e.getMessage() != null && e.getMessage().contains("Payment service temporarily unavailable")) {
                return CommonResult.failed("Payment service temporarily unavailable");
            }
            return CommonResult.failed("Failed to create Razorpay order");
        }
    }

    private final com.nexusengine.core.repository.OmsOrderRepository omsOrderRepository;

    @Operation(summary = "Verify Razorpay Payment")
    @PostMapping("/verifyRazorpayPayment")

    public CommonResult verifyRazorpayPayment(@RequestParam Long orderId, 
                                              @RequestParam String razorpayPaymentId,
                                              @RequestParam String razorpayOrderId,
                                              @RequestParam String razorpaySignature) {
        try {
            com.nexusengine.core.portal.domain.OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
            if (orderDetail == null) return CommonResult.failed("Order not found");
            // Check ownership and status
            com.nexusengine.core.model.UmsMember currentMember = memberService.getCurrentMember();
            if (!orderDetail.getMemberId().equals(currentMember.getId())) {
                return CommonResult.failed("Order ownership verification failed");
            }
            if (orderDetail.getStatus() != 0) {
                 return CommonResult.failed("Order already paid or closed");
            }
            if (omsOrderRepository.existsByPaymentId(razorpayPaymentId)) {
                return CommonResult.failed("Payment ID already used");
            }
            
            boolean isValid = razorpayPaymentGatewayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            if (!isValid) return CommonResult.failed("Invalid signature");
            
            int expectedAmount = orderDetail.getPayAmount().multiply(new java.math.BigDecimal("100")).intValue();
            boolean isAmountValid = razorpayPaymentGatewayService.verifyPaymentAmount(razorpayPaymentId, expectedAmount);
            if (!isAmountValid) return CommonResult.failed("Payment amount does not match order amount");
            
            // Store paymentId to prevent replay
            com.nexusengine.core.model.OmsOrder dbOrder = omsOrderRepository.findById(orderId).orElse(null);
            if (dbOrder != null) {
                dbOrder.setPaymentId(razorpayPaymentId);
                omsOrderRepository.save(dbOrder);
            }
            
            portalOrderService.paySuccess(orderId, 2);
            return CommonResult.success("Payment successful");
        } catch (org.springframework.dao.DataAccessException e) {
            log.error("Database error during payment verification", e);
            return CommonResult.failed("Payment verification failed due to database error");
        } catch (RuntimeException e) {
            log.error("Payment verification failed", e);
            return CommonResult.failed("Payment verification failed");
        }
    }

    @Operation(summary = "Razorpay Webhook Endpoint")
    @PostMapping("/webhook")
    public CommonResult handlePaymentWebhook(
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature,
            @RequestBody String payload) {
        try {
            log.info("Received Razorpay Webhook: {}", payload);
            portalOrderService.handlePaymentWebhook(payload, signature);
            return CommonResult.success("Webhook processed successfully");
        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            // Return 200 even on failure so Razorpay doesn't blindly retry badly formed requests, 
            // or 500 if we want retries. We will return failed but HTTP 200.
            return CommonResult.failed(e.getMessage());
        }
    }

}
