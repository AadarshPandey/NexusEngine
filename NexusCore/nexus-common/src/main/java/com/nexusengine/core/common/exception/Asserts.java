package com.nexusengine.core.common.exception;

import com.nexusengine.core.common.api.IErrorCode;

/**
 * Represents the Asserts component.
 * Provides core functionality and operations for Asserts.
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
