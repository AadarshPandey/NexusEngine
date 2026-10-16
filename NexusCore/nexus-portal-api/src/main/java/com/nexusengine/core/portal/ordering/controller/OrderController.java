package com.nexusengine.core.portal.ordering.controller;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.ConfirmOrderResult;
import com.nexusengine.core.portal.ordering.dto.OrderConfirmationResponse;
import com.nexusengine.core.portal.ordering.dto.OrderDetail;
import com.nexusengine.core.portal.ordering.dto.OrderParam;
import com.nexusengine.core.portal.ordering.dto.PaymentInitiationResponse;
import com.nexusengine.core.portal.ordering.service.OrderService;
import com.nexusengine.core.portal.payment.service.PaymentGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("portalOrderController")
@Tag(name = "Storefront - Orders", description = "Storefront order and checkout management APIs")
@RequestMapping("/portal/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService portalOrderService;
    private final MemberService memberService;
    private final PaymentGatewayService paymentGatewayService;
    private final CustomerOrderRepository customerOrderRepository;

    @Operation(summary = "Generate order confirmation preview")
    @PostMapping("/generateConfirmOrder")
    public ConfirmOrderResult generateConfirmOrder(@Valid @RequestBody List<Long> cartIds) {
        return portalOrderService.generateConfirmOrder(cartIds);
    }

    @Operation(summary = "Place order")
    @PostMapping("/generateOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderConfirmationResponse generateOrder(@Valid @RequestBody OrderParam orderParam) {
        return portalOrderService.generateOrder(orderParam);
    }

    @Operation(summary = "List member orders by status")
    @Parameter(
            name = "status",
            description = "Order status (-1 for all)",
            in = ParameterIn.QUERY,
            schema =
                    @Schema(
                            type = "integer",
                            defaultValue = "-1",
                            allowableValues = {"-1", "0", "1", "2", "3", "4"}))
    @GetMapping("/list")
    public Page<OrderDetail> list(
            @RequestParam("status") Integer status,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return portalOrderService.list(status, pageNum, pageSize);
    }

    @Operation(summary = "Get order detail")
    @GetMapping("/detail/{orderId}")
    public OrderDetail detail(@PathVariable("orderId") Long orderId) {
        OrderDetail orderDetail = portalOrderService.detail(orderId);
        if (orderDetail == null) {
            throw new ResourceNotFoundException("Order", orderId);
        }
        return orderDetail;
    }

    @Operation(summary = "Cancel member order")
    @PostMapping("/cancelUserOrder")
    public void cancelUserOrder(@RequestParam("orderId") Long orderId) {
        portalOrderService.cancelOrder(orderId);
    }

    @Operation(summary = "Confirm order receipt")
    @PostMapping("/confirmReceiveOrder")
    public void confirmReceiveOrder(@RequestParam("orderId") Long orderId) {
        portalOrderService.confirmReceiveOrder(orderId);
    }

    @Operation(summary = "Delete order")
    @PostMapping("/deleteOrder")
    public void deleteOrder(@RequestParam("orderId") Long orderId) {
        portalOrderService.deleteOrder(orderId);
    }

    @Operation(summary = "Create Razorpay payment order")
    @PostMapping("/createRazorpayOrder")
    public PaymentInitiationResponse createRazorpayOrder(
            @RequestParam("orderId") Long orderId) {
        OrderDetail orderDetail = portalOrderService.detail(orderId);
        if (orderDetail == null) {
            throw new ResourceNotFoundException("Order", orderId);
        }
        int amount =
                orderDetail
                        .customerOrder()
                        .getPayAmount()
                        .multiply(new BigDecimal("100"))
                        .intValue();
        return paymentGatewayService.createOrder(amount, orderDetail.customerOrder().getOrderSn());
    }

    @Operation(summary = "Verify Razorpay payment")
    @PostMapping("/verifyRazorpayPayment")
    public String verifyRazorpayPayment(
            @RequestParam("orderId") Long orderId,
            @RequestParam("razorpayPaymentId") String razorpayPaymentId,
            @RequestParam("razorpayOrderId") String razorpayOrderId,
            @RequestParam("razorpaySignature") String razorpaySignature) {
        OrderDetail orderDetail = portalOrderService.detail(orderId);
        if (orderDetail == null) {
            throw new ResourceNotFoundException("Order", orderId);
        }
        Member currentMember = memberService.getCurrentMember();
        if (!orderDetail.customerOrder().getMemberId().equals(currentMember.getId())) {
            Asserts.fail("Order ownership verification failed");
        }
        if (orderDetail.customerOrder().getStatus() != 0) {
            Asserts.fail("Order already paid or closed");
        }

        boolean isValid =
                paymentGatewayService.verifySignature(
                        razorpayOrderId, razorpayPaymentId, razorpaySignature);
        if (!isValid) {
            Asserts.fail("Invalid signature");
        }

        int expectedAmount =
                orderDetail
                        .customerOrder()
                        .getPayAmount()
                        .multiply(new BigDecimal("100"))
                        .intValue();
        boolean isAmountValid =
                paymentGatewayService.verifyPaymentAmount(razorpayPaymentId, expectedAmount);
        if (!isAmountValid) {
            Asserts.fail("Payment amount does not match order amount");
        }

        Integer payResult = portalOrderService.paySuccess(orderId, 2, razorpayPaymentId);
        if (payResult == null || payResult <= 0) {
            Asserts.fail("Payment processing failed or already paid");
        }
        return "Payment successful";
    }

    @Operation(summary = "Razorpay Webhook Endpoint")
    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaymentWebhook(
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature,
            @RequestBody String payload) {
        if (signature == null || signature.isBlank()) {
            log.warn("Rejected Razorpay webhook missing signature header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing X-Razorpay-Signature header");
        }
        if (!paymentGatewayService.verifyWebhookSignature(payload, signature)) {
            log.warn("Rejected Razorpay webhook with invalid signature");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid webhook signature");
        }
        try {
            log.info("Processing verified Razorpay Webhook");
            portalOrderService.handlePaymentWebhook(payload, signature);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            log.error("Verified webhook processing failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Webhook processing failed");
        }
    }
}
