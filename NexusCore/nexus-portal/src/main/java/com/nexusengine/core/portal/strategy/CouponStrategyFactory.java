package com.nexusengine.core.portal.strategy;

public class CouponStrategyFactory {
    public static CouponDiscountStrategy getStrategy(Integer type) {
        if (type == null) {
            return new FixedAmountStrategy();
        }
        return switch (type) {
            case 0 -> new FixedAmountStrategy();
            case 1 -> new PercentageStrategy();
            case 2 -> new FreeShippingStrategy();
            default -> throw new IllegalArgumentException("Unknown coupon type: " + type);
        };
    }
}
