package com.nexusengine.core.common.exception;

import com.nexusengine.core.common.api.IErrorCode;

/**
 * Auto-generated documentation
 * Created by macro on 2020/2/27.
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
