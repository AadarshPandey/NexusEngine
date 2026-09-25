package com.nexusengine.core.portal.strategy;

import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.SmsCoupon;
import java.math.BigDecimal;
import java.util.List;

public class FixedAmountStrategy implements CouponDiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(List<OmsOrderItem> items, SmsCoupon coupon) {
        return coupon.getAmount();
    }
}
