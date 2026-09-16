package com.nexusengine.core.portal.service;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.portal.domain.ConfirmOrderResult;
import com.nexusengine.core.portal.domain.OmsOrderDetail;
import com.nexusengine.core.portal.domain.OrderParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing portal orders.
 * Handles order creation, payment success, cancellation, and retrieval.
 */
public interface OmsPortalOrderService {
/**
     * Generates a confirmation result for a list of cart items.
     *
     * @param cartIds the list of cart item IDs
     * @return the confirmation order result containing order details
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

/**
     * Generates a new order based on the provided parameters.
     *
     * @param orderParam the order creation parameters
     * @return a map containing order generation results
     */
    @Transactional
    Map<String, Object> generateOrder(OrderParam orderParam);

/**
     * Processes a successful payment for an order.
     *
     * @param orderId the unique identifier of the order
     * @param payType the payment type used
     * @return the status of the operation
     */
    @Transactional
    Integer paySuccess(Long orderId, Integer payType);

/**
     * Cancels all orders that have timed out pending payment.
     *
     * @return the number of canceled orders
     */
    @Transactional
    Integer cancelTimeOutOrder();

/**
     * Cancels a specific order manually.
     *
     * @param orderId the unique identifier of the order to cancel
     */
    @Transactional
    void cancelOrder(Long orderId);

/**
     * Sends a delayed message to automatically cancel an order if unpaid.
     *
     * @param orderId the unique identifier of the order
     */
    void sendDelayMessageCancelOrder(Long orderId);

/**
     * Confirms the receipt of an order by the user.
     *
     * @param orderId the unique identifier of the order
     */
    void confirmReceiveOrder(Long orderId);

/**
     * Retrieves a paginated list of orders filtered by status.
     *
     * @param status the order status filter
     * @param pageNum the current page number
     * @param pageSize the number of items per page
     * @return a paginated list of order details
     */
    CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize);

/**
     * Retrieves the detailed information of a specific order.
     *
     * @param orderId the unique identifier of the order
     * @return the comprehensive order details
     */
    OmsOrderDetail detail(Long orderId);

/**
     * Deletes a specific order from the system.
     *
     * @param orderId the unique identifier of the order to delete
     */
    void deleteOrder(Long orderId);

/**
     * Processes a successful payment using the order serial number.
     *
     * @param orderSn the unique serial number of the order
     * @param payType the payment type used
     */
    @Transactional
    void paySuccessByOrderSn(String orderSn, Integer payType);
    void handlePaymentWebhook(String payload, String signature);
}
