package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.portal.ordering.dto.ConfirmOrderResult;
import com.nexusengine.core.portal.ordering.dto.OrderConfirmationResponse;
import com.nexusengine.core.portal.ordering.dto.OrderDetail;
import com.nexusengine.core.portal.ordering.dto.OrderParam;
import java.util.List;
import org.springframework.data.domain.Page;

public interface OrderService {
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

    OrderConfirmationResponse generateOrder(OrderParam orderParam);

    Integer paySuccess(Long orderId, Integer payType);

    Integer paySuccess(Long orderId, Integer payType, String paymentId);

    Integer cancelTimeOutOrder();

    void cancelOrder(Long orderId);

    void sendDelayMessageCancelOrder(Long orderId);

    void confirmReceiveOrder(Long orderId);

    Page<OrderDetail> list(Integer status, Integer pageNum, Integer pageSize);

    OrderDetail detail(Long orderId);

    void deleteOrder(Long orderId);

    void paySuccessByOrderSn(String orderSn, Integer payType);

    void handlePaymentWebhook(String payload, String signature);

    int autoConfirmShippedOrders();
}
