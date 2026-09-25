package com.nexusengine.core.portal.strategy;

import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.SmsCoupon;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PercentageStrategy implements CouponDiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(List<OmsOrderItem> items, SmsCoupon coupon) {
        BigDecimal total = calcEligibleTotal(items);
        BigDecimal discount = total.multiply(coupon.getAmount())
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
        if (coupon.getMaxDiscountAmount() != null && coupon.getMaxDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            discount = discount.min(coupon.getMaxDiscountAmount());
        }
        return discount;
    }
    
    private BigDecimal calcEligibleTotal(List<OmsOrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OmsOrderItem item : items) {
            total = total.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return total;
    }
}
