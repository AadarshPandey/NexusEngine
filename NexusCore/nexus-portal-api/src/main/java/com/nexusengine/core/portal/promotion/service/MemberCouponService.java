package com.nexusengine.core.portal.promotion.service;

import com.nexusengine.core.domain.promotion.entity.Coupon;

import com.nexusengine.core.domain.promotion.entity.MemberCoupon;

import java.util.List;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.promotion.dto.CouponHistoryDetail;


/**
 * Represents the MemberCouponService component.
 * Provides core functionality and operations for MemberCouponService.
 */
public interface MemberCouponService {
        /**
     * Executes the operation.
     * @param couponId the couponId
     */
    void add(Long couponId);

        /**
     * Executes the operation.
     * @param useStatus the useStatus
     * @return the result of the operation
     */
    List<MemberCoupon> listHistory(Integer useStatus);

        /**
     * Executes the operation.
     * @param cartItemList the cartItemList
     * @param type the type
     * @return the result of the operation
     */
    List<CouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type);

        /**
     * Executes the operation.
     * @param productId the productId
     * @return the result of the operation
     */
    List<Coupon> listByProduct(Long productId);

        /**
     * Executes the operation.
     * @param useStatus the useStatus
     * @return the result of the operation
     */
    List<Coupon> list(Integer useStatus);
}
