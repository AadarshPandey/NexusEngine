package com.nexusengine.core.common.api;

/**
 * Auto-generated documentation
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "Success"),
    FAILED(500, "Success"),
    VALIDATE_FAILED(404, "Success"),
    UNAUTHORIZED(401, "Success"),
    FORBIDDEN(403, "Success");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
