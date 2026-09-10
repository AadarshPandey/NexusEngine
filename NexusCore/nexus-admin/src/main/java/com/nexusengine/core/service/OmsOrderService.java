package com.nexusengine.core.service;

import com.nexusengine.core.dto.*;
import com.nexusengine.core.model.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the OmsOrderService component.
 * Provides core functionality and operations for OmsOrderService.
 */
public interface OmsOrderService {
        /**
     * Executes the operation.
     * @param queryParam the queryParam
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param deliveryParamList the deliveryParamList
     * @return the result of the operation
     */
    @Transactional
    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

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
    OmsOrderDetail detail(Long id);

        /**
     * Executes the operation.
     * @param receiverInfoParam the receiverInfoParam
     * @return the result of the operation
     */
    @Transactional
    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

        /**
     * Executes the operation.
     * @param moneyInfoParam the moneyInfoParam
     * @return the result of the operation
     */
    @Transactional
    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

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
