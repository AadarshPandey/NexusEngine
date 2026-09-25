package com.nexusengine.core.portal.unit.strategy;

import com.nexusengine.core.portal.strategy.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CouponStrategyFactoryTest {

    @Test
    void getStrategy_Type0_ReturnsFixedAmount() {
        assertTrue(CouponStrategyFactory.getStrategy(0) instanceof FixedAmountStrategy);
    }

    @Test
    void getStrategy_Type1_ReturnsPercentage() {
        assertTrue(CouponStrategyFactory.getStrategy(1) instanceof PercentageStrategy);
    }

    @Test
    void getStrategy_Type2_ReturnsFreeShipping() {
        assertTrue(CouponStrategyFactory.getStrategy(2) instanceof FreeShippingStrategy);
    }

    @Test
    void getStrategy_NullType_DefaultsToFixedAmount() {
        assertTrue(CouponStrategyFactory.getStrategy(null) instanceof FixedAmountStrategy);
    }

    @Test
    void getStrategy_UnknownType_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> CouponStrategyFactory.getStrategy(99));
    }
}
