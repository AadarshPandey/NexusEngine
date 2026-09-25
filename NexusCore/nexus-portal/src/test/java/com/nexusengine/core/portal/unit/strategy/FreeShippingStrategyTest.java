package com.nexusengine.core.portal.unit.strategy;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.portal.strategy.FreeShippingStrategy;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FreeShippingStrategyTest {

    @Test
    void calculateDiscount_AlwaysReturnsZero() {
        FreeShippingStrategy strategy = new FreeShippingStrategy();
        SmsCoupon coupon = new SmsCoupon();
        
        BigDecimal discount = strategy.calculateDiscount(Collections.emptyList(), coupon);

        assertEquals(BigDecimal.ZERO, discount);
    }
}
