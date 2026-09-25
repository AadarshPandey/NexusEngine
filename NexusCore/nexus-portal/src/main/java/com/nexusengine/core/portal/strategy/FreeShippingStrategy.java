package com.nexusengine.core.portal.strategy;

import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.SmsCoupon;
import java.math.BigDecimal;
import java.util.List;

public class FreeShippingStrategy implements CouponDiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(List<OmsOrderItem> items, SmsCoupon coupon) {
        return BigDecimal.ZERO; // Freight is handled at the order level
    }
}
