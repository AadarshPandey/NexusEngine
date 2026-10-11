package com.nexusengine.core.admin.ordering.service;



import com.nexusengine.core.domain.ordering.entity.CustomerOrder;

import com.nexusengine.core.admin.ordering.dto.OrderDeliveryParam;
import com.nexusengine.core.admin.ordering.dto.OrderDetail;
import com.nexusengine.core.admin.ordering.dto.OrderMoneyInfoParam;
import com.nexusengine.core.admin.ordering.dto.OrderQueryParam;
import com.nexusengine.core.admin.ordering.dto.OrderReceiverInfoParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the OrderService component.
 * Provides core functionality and operations for OrderService.
 */
public interface OrderService {
        /**
     * Executes the operation.
     * @param queryParam the queryParam
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<CustomerOrder> list(OrderQueryParam queryParam, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param deliveryParamList the deliveryParamList
     * @return the result of the operation
     */
    @Transactional
    int delivery(List<OrderDeliveryParam> deliveryParamList);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param note the note
     * @return the result of the operation
     */
    @Transactional
    int close(List<Long> ids, String note);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    OrderDetail detail(Long id);

        /**
     * Executes the operation.
     * @param receiverInfoParam the receiverInfoParam
     * @return the result of the operation
     */
    @Transactional
    int updateReceiverInfo(OrderReceiverInfoParam receiverInfoParam);

        /**
     * Executes the operation.
     * @param moneyInfoParam the moneyInfoParam
     * @return the result of the operation
     */
    @Transactional
    int updateMoneyInfo(OrderMoneyInfoParam moneyInfoParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param note the note
     * @param status the status
     * @return the result of the operation
     */
    @Transactional
    int updateNote(Long id, String note, Integer status);

    /**
     * Update order status
     */
    @Transactional
    int updateStatus(List<Long> ids, Integer status, String note);
}
