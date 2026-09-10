package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.OmsOrderReturnApplyParam;

/**
 * Represents the OmsPortalOrderReturnApplyService component.
 * Provides core functionality and operations for OmsPortalOrderReturnApplyService.
 */
public interface OmsPortalOrderReturnApplyService {
        /**
     * Executes the operation.
     * @param returnApply the returnApply
     */
    int create(OmsOrderReturnApplyParam returnApply);
}
