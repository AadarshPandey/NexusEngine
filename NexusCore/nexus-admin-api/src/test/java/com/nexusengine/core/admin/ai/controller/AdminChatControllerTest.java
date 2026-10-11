package com.nexusengine.core.admin.ai.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AdminChatControllerTest {

    @InjectMocks
    private AdminChatController controller;

    @Test
    void testContextLoads() {
        assertNotNull(controller, "Controller should be instantiated");
    }
}
