package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.domain.SmsCouponHistoryDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/29.
 */
public interface UmsMemberCouponService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    void add(Long couponId);

    /**
     * Auto-generated documentation
     */
    List<SmsCouponHistory> listHistory(Integer useStatus);

    /**
     * Auto-generated documentation
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type);

    /**
     * Auto-generated documentation
     */
    List<SmsCoupon> listByProduct(Long productId);

    /**
     * Auto-generated documentation
     */
    List<SmsCoupon> list(Integer useStatus);
}
