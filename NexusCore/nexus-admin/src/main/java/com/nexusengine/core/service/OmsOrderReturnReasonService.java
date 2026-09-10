package com.nexusengine.core.service;

import com.nexusengine.core.model.OmsOrderReturnReason;

import java.util.List;

/**
 * Represents the OmsOrderReturnReasonService component.
 * Provides core functionality and operations for OmsOrderReturnReasonService.
 */
public interface OmsOrderReturnReasonService {
        /**
     * Executes the operation.
     * @param returnReason the returnReason
     */
    int create(OmsOrderReturnReason returnReason);

        /**
     * Executes the operation.
     * @param id the id
     * @param returnReason the returnReason
     */
    int update(Long id, OmsOrderReturnReason returnReason);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param status the status
     * @return the result of the operation
     */
    int updateStatus(List<Long> ids, Integer status);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    OmsOrderReturnReason getItem(Long id);
}
