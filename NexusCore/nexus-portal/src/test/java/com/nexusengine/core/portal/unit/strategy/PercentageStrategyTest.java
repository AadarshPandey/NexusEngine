package com.nexusengine.core.portal.unit.strategy;

import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.portal.strategy.PercentageStrategy;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PercentageStrategyTest {

    @Test
    void calculateDiscount_10PercentOnTwoItems_ReturnsCorrectDiscount() {
        PercentageStrategy strategy = new PercentageStrategy();
        SmsCoupon coupon = new SmsCoupon();
        coupon.setAmount(new BigDecimal("10")); // 10%

        OmsOrderItem item1 = new OmsOrderItem();
        item1.setProductPrice(new BigDecimal("100"));
        item1.setProductQuantity(2); // 200

        OmsOrderItem item2 = new OmsOrderItem();
        item2.setProductPrice(new BigDecimal("50"));
        item2.setProductQuantity(1); // 50

        // Total = 250. 10% of 250 = 25.00
        BigDecimal discount = strategy.calculateDiscount(Arrays.asList(item1, item2), coupon);

        assertEquals(new BigDecimal("25.00"), discount);
    }

    @Test
    void calculateDiscount_WithMaxCap_RespectsMaxDiscountAmount() {
        PercentageStrategy strategy = new PercentageStrategy();
        SmsCoupon coupon = new SmsCoupon();
        coupon.setAmount(new BigDecimal("50")); // 50%
        coupon.setMaxDiscountAmount(new BigDecimal("100.00")); // Max cap

        OmsOrderItem item1 = new OmsOrderItem();
        item1.setProductPrice(new BigDecimal("500"));
        item1.setProductQuantity(1); // 500

        // 50% of 500 = 250, but cap is 100
        BigDecimal discount = strategy.calculateDiscount(Collections.singletonList(item1), coupon);

        assertEquals(new BigDecimal("100.00"), discount);
    }
}
