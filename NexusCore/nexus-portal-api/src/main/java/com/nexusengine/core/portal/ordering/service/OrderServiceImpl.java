package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.portal.ordering.dto.ConfirmOrderResult;
import com.nexusengine.core.portal.ordering.dto.OrderConfirmationResponse;
import com.nexusengine.core.portal.ordering.dto.OrderDetail;
import com.nexusengine.core.portal.ordering.dto.OrderParam;
import com.nexusengine.core.portal.payment.service.PaymentWebhookHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Portal order facade delegating to focused domain services:
 * CheckoutService, OrderLifecycleService, and PaymentWebhookHandler.
 */
@Service("portalOrderServiceImpl")
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CheckoutService checkoutService;
    private final OrderLifecycleService orderLifecycleService;
    private final PaymentWebhookHandler paymentWebhookHandler;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        return checkoutService.generateConfirmOrder(cartIds);
    }

    @Override
    public OrderConfirmationResponse generateOrder(OrderParam orderParam) {
        return checkoutService.generateOrder(orderParam);
    }

    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        return orderLifecycleService.paySuccess(orderId, payType);
    }

    @Override
    public Integer paySuccess(Long orderId, Integer payType, String paymentId) {
        return orderLifecycleService.paySuccess(orderId, payType, paymentId);
    }

    @Override
    public void paySuccessByOrderSn(String orderSn, Integer payType) {
        orderLifecycleService.paySuccessByOrderSn(orderSn, payType);
    }

    @Override
    public Integer cancelTimeOutOrder() {
        return orderLifecycleService.cancelTimeOutOrder();
    }

    @Override
    public void cancelOrder(Long orderId) {
        orderLifecycleService.cancelOrder(orderId);
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        checkoutService.sendDelayMessageCancelOrder(orderId);
    }

    @Override
    public void confirmReceiveOrder(Long orderId) {
        orderLifecycleService.confirmReceiveOrder(orderId);
    }

    @Override
    public Page<OrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        return orderLifecycleService.list(status, pageNum, pageSize);
    }

    @Override
    public OrderDetail detail(Long orderId) {
        return orderLifecycleService.detail(orderId);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderLifecycleService.deleteOrder(orderId);
    }

    @Override
    public void handlePaymentWebhook(String payload, String signature) {
        paymentWebhookHandler.handlePaymentWebhook(payload, signature);
    }

    @Override
    public int autoConfirmShippedOrders() {
        return orderLifecycleService.autoConfirmShippedOrders();
    }
}
