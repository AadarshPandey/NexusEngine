package com.nexusengine.core.portal.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Auto-generated documentation
 */
@Controller
@Tag(name = "OmsPortalOrderController", description = "Oms portal order controller APIs")
@RequestMapping("/portal/order")
@lombok.extern.slf4j.Slf4j
public class OmsPortalOrderController {
    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Operation(summary = "Generate confirm order Operation")
    @RequestMapping(value = "/generateConfirmOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ConfirmOrderResult> generateConfirmOrder(@RequestBody List<Long> cartIds) {
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(cartIds);
        return CommonResult.success(confirmOrderResult);
    }

    @Operation(summary = "Generate order Operation")
    @RequestMapping(value = "/generateOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateOrder(@RequestBody OrderParam orderParam) {
        Map<String, Object> result = portalOrderService.generateOrder(orderParam);
        return CommonResult.success(result, "Success");
    }

    @Operation(summary = "Pay success Operation")
    @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult paySuccess(@RequestParam Long orderId,@RequestParam Integer payType) {
        Integer count = portalOrderService.paySuccess(orderId,payType);
        return CommonResult.success(count, "Success");
    }

    @Operation(summary = "Cancel time out order Operation")
    @RequestMapping(value = "/cancelTimeOutOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelTimeOutOrder() {
        portalOrderService.cancelTimeOutOrder();
        return CommonResult.success(null);
    }

    @Operation(summary = "Cancel order Operation")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelOrder(Long orderId) {
        portalOrderService.sendDelayMessageCancelOrder(orderId);
        return CommonResult.success(null);
    }

    @Operation(summary = "API Operation")
    @Parameter(name = "status", description = "Description",
            in = ParameterIn.QUERY, schema = @Schema(type = "integer",defaultValue = "-1",allowableValues = {"-1","0","1","2","3","4"}))
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<OmsOrderDetail>> list(@RequestParam Integer status,
                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        CommonPage<OmsOrderDetail> orderPage = portalOrderService.list(status,pageNum,pageSize);
        return CommonResult.success(orderPage);
    }

    @Operation(summary = "Detail Operation")
    @RequestMapping(value = "/detail/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OmsOrderDetail> detail(@PathVariable Long orderId) {
        OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
        return CommonResult.success(orderDetail);
    }

    @Operation(summary = "Cancel user order Operation")
    @RequestMapping(value = "/cancelUserOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelUserOrder(Long orderId) {
        portalOrderService.cancelOrder(orderId);
        return CommonResult.success(null);
    }

    @Operation(summary = "Confirm receive order Operation")
    @RequestMapping(value = "/confirmReceiveOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult confirmReceiveOrder(Long orderId) {
        portalOrderService.confirmReceiveOrder(orderId);
        return CommonResult.success(null);
    }

    @Operation(summary = "Delete order Operation")
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteOrder(Long orderId) {
        portalOrderService.deleteOrder(orderId);
        return CommonResult.success(null);
    }
    @Autowired
    private com.nexusengine.core.portal.service.RazorpayPaymentGatewayService razorpayPaymentGatewayService;

    @Operation(summary = "Create Razorpay Order")
    @RequestMapping(value = "/createRazorpayOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Map<String, String>> createRazorpayOrder(@RequestParam Long orderId) {
        try {
            OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
            if (orderDetail == null) return CommonResult.failed("Order not found");
            
            int amount = orderDetail.getPayAmount().multiply(new java.math.BigDecimal("100")).intValue();
            Map<String, String> result = razorpayPaymentGatewayService.createOrder(amount, orderDetail.getOrderSn());
            
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("Failed to create Razorpay order", e);
            if (e.getMessage() != null && e.getMessage().contains("Payment service temporarily unavailable")) {
                return CommonResult.failed("Payment service temporarily unavailable");
            }
            return CommonResult.failed("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    @Operation(summary = "Verify Razorpay Payment")
    @RequestMapping(value = "/verifyRazorpayPayment", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult verifyRazorpayPayment(@RequestParam Long orderId, 
                                              @RequestParam String razorpayPaymentId,
                                              @RequestParam String razorpayOrderId,
                                              @RequestParam String razorpaySignature) {
        try {
            boolean isValid = razorpayPaymentGatewayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            if (isValid) {
                portalOrderService.paySuccess(orderId, 2);
                return CommonResult.success("Payment successful");
            } else {
                return CommonResult.failed("Invalid signature");
            }
        } catch (Exception e) {
            log.error("Payment verification failed", e);
            return CommonResult.failed("Payment verification failed");
        }
    }
}
