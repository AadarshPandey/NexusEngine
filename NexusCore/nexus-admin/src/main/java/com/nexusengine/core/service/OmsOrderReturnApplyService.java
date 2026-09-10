package com.nexusengine.core.service;

import com.nexusengine.core.dto.OmsOrderReturnApplyResult;
import com.nexusengine.core.dto.OmsReturnApplyQueryParam;
import com.nexusengine.core.dto.OmsUpdateStatusParam;
import com.nexusengine.core.model.OmsOrderReturnApply;

import java.util.List;

/**
 * Represents the OmsOrderReturnApplyService component.
 * Provides core functionality and operations for OmsOrderReturnApplyService.
 */
public interface OmsOrderReturnApplyService {
        /**
     * Executes the operation.
     * @param queryParam the queryParam
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

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
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
