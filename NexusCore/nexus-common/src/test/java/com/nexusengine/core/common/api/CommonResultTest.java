package com.nexusengine.core.common.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommonResultTest {

    @Test
    void testSuccess() {
        CommonResult<String> result = CommonResult.success("test data");
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertEquals("test data", result.getData());
    }

    @Test
    void testSuccessWithMessage() {
        CommonResult<String> result = CommonResult.success("test data", "Custom Message");
        assertEquals(200, result.getCode());
        assertEquals("Custom Message", result.getMessage());
        assertEquals("test data", result.getData());
    }

    @Test
    void testFailed() {
        CommonResult<String> result = CommonResult.failed();
        assertEquals(500, result.getCode());
        assertEquals("Failed", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testFailedWithMessage() {
        CommonResult<String> result = CommonResult.failed("Custom Error");
        assertEquals(500, result.getCode());
        assertEquals("Custom Error", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testValidateFailed() {
        CommonResult<String> result = CommonResult.validateFailed();
        assertEquals(404, result.getCode());
        assertEquals("Validate Failed", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testUnauthorized() {
        CommonResult<String> result = CommonResult.unauthorized("auth info");
        assertEquals(401, result.getCode());
        assertEquals("Unauthorized", result.getMessage());
        assertEquals("auth info", result.getData());
    }

    @Test
    void testForbidden() {
        CommonResult<String> result = CommonResult.forbidden("forbidden info");
        assertEquals(403, result.getCode());
        assertEquals("Forbidden", result.getMessage());
        assertEquals("forbidden info", result.getData());
    }
}
