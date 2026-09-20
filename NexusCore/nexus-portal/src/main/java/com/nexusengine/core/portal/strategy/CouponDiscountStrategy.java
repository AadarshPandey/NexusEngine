package com.nexusengine.core.portal.strategy;

import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.SmsCoupon;
import java.math.BigDecimal;
import java.util.List;

public interface CouponDiscountStrategy {
    BigDecimal calculateDiscount(List<OmsOrderItem> items, SmsCoupon coupon);
}
