package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.domain.SmsCouponHistoryDetail;

import java.util.List;

/**
 * Represents the UmsMemberCouponService component.
 * Provides core functionality and operations for UmsMemberCouponService.
 */
public interface UmsMemberCouponService {
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
    List<SmsCouponHistory> listHistory(Integer useStatus);

        /**
     * Executes the operation.
     * @param cartItemList the cartItemList
     * @param type the type
     * @return the result of the operation
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type);

        /**
     * Executes the operation.
     * @param productId the productId
     * @return the result of the operation
     */
    List<SmsCoupon> listByProduct(Long productId);

        /**
     * Executes the operation.
     * @param useStatus the useStatus
     * @return the result of the operation
     */
    List<SmsCoupon> list(Integer useStatus);
}
