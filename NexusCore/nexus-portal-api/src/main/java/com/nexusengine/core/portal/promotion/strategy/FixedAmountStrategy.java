package com.nexusengine.core.portal.promotion.strategy;

import com.nexusengine.core.domain.ordering.entity.OrderLine;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import java.math.BigDecimal;
import java.util.List;


public class FixedAmountStrategy implements CouponDiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(List<OrderLine> items, Coupon coupon) {
        return coupon.getAmount();
    }
}
