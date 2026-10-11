package com.nexusengine.core.admin.promotion.service;

import com.nexusengine.core.domain.promotion.entity.MemberCoupon;



import java.util.List;


/**
 * Represents the CouponHistoryService component.
 * Provides core functionality and operations for CouponHistoryService.
 */
public interface CouponHistoryService {
        /**
     * Executes the operation.
     * @param couponId the couponId
     * @param useStatus the useStatus
     * @param orderSn the orderSn
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<MemberCoupon> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
