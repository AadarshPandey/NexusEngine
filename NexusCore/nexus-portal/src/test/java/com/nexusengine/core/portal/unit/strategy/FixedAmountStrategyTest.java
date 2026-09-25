package com.nexusengine.core.portal.unit.strategy;

import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.portal.strategy.FixedAmountStrategy;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedAmountStrategyTest {

    @Test
    void calculateDiscount_ReturnsFixedAmount_RegardlessOfItems() {
        FixedAmountStrategy strategy = new FixedAmountStrategy();
        SmsCoupon coupon = new SmsCoupon();
        coupon.setAmount(new BigDecimal("50.00"));

        BigDecimal discount = strategy.calculateDiscount(Collections.emptyList(), coupon);

        assertEquals(new BigDecimal("50.00"), discount);
    }
}
