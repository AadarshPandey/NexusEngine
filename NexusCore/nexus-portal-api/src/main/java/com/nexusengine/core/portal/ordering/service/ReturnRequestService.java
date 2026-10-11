package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.portal.ordering.dto.ReturnApplyParam;




/**
 * Represents the ReturnRequestService component.
 * Provides core functionality and operations for ReturnRequestService.
 */
public interface ReturnRequestService {
        /**
     * Executes the operation.
     * @param returnApply the returnApply
     */
    int create(ReturnApplyParam returnApply);
}
