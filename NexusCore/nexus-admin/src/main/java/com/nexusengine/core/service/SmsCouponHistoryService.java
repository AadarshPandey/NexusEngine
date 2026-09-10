package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsCouponHistory;

import java.util.List;

/**
 * Represents the SmsCouponHistoryService component.
 * Provides core functionality and operations for SmsCouponHistoryService.
 */
public interface SmsCouponHistoryService {
        /**
     * Executes the operation.
     * @param couponId the couponId
     * @param useStatus the useStatus
     * @param orderSn the orderSn
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
