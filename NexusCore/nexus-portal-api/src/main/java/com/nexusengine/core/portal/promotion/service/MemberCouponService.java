package com.nexusengine.core.portal.promotion.service;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;

import java.util.List;


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
