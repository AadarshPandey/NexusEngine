package com.nexusengine.core.admin.promotion.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CouponHistoryControllerTest {

    @InjectMocks
    private CouponHistoryController controller;

    @Test
    void testContextLoads() {
        assertNotNull(controller, "Controller should be instantiated");
    }
}
