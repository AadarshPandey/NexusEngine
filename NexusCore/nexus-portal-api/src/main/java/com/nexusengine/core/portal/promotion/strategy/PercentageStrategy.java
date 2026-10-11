package com.nexusengine.core.portal.promotion.strategy;

import com.nexusengine.core.domain.ordering.entity.OrderLine;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class PercentageStrategy implements CouponDiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(List<OrderLine> items, Coupon coupon) {
        BigDecimal total = calcEligibleTotal(items);
        BigDecimal discount = total.multiply(coupon.getAmount())
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
        if (coupon.getMaxDiscountAmount() != null && coupon.getMaxDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            discount = discount.min(coupon.getMaxDiscountAmount());
        }
        return discount;
    }
    
    private BigDecimal calcEligibleTotal(List<OrderLine> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine item : items) {
            total = total.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return total;
    }
}
