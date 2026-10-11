package com.nexusengine.core.admin.ordering.service;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;

import com.nexusengine.core.admin.ordering.dto.OrderStatusUpdateParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestQueryParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestResult;

import java.util.List;


/**
 * Represents the ReturnRequestService component.
 * Provides core functionality and operations for ReturnRequestService.
 */
public interface ReturnRequestService {
        /**
     * Executes the operation.
     * @param queryParam the queryParam
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<ReturnRequest> list(ReturnRequestQueryParam queryParam, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param id the id
     * @param statusParam the statusParam
     * @return the result of the operation
     */
    int updateStatus(Long id, OrderStatusUpdateParam statusParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ReturnRequestResult getItem(Long id);
}
