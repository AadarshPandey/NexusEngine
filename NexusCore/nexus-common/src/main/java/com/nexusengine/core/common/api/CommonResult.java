package com.nexusengine.core.common.api;

/**
 * A generic wrapper class for API responses.
 * Standardizes the structure of responses with code, message, and data fields.
 *
 * @param <T> the type of the payload data
 */
public class CommonResult<T> {
    /**
     * The status code of the response.
     */
    private long code;
    /**
     * The descriptive message associated with the response status.
     */
    private String message;
    /**
     * The payload data of the response.
     */
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Returns a successful response with the given data.
     *
     * @param data the payload data
     * @param <T> the type of the payload
     * @return a successful CommonResult
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * Returns a successful response with the given data and a custom message.
     *
     * @param data the payload data
     * @param message the custom success message
     * @param <T> the type of the payload
     * @return a successful CommonResult
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

/**
     * Returns a failed response based on the provided error code.
     *
     * @param errorCode the error code interface implementation
     * @param <T> the type of the payload
     * @return a failed CommonResult
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

/**
     * Returns a failed response based on the provided error code and a custom message.
     *
     * @param errorCode the error code interface implementation
     * @param message the custom failure message
     * @param <T> the type of the payload
     * @return a failed CommonResult
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode,String message) {
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

/**
     * Returns a failed response with a custom failure message.
     *
     * @param message the custom failure message
     * @param <T> the type of the payload
     * @return a failed CommonResult
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

/**
     * Returns a generic failed response with default failure code and message.
     *
     * @param <T> the type of the payload
     * @return a failed CommonResult
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

/**
     * Returns a failed response indicating validation failure with default settings.
     *
     * @param <T> the type of the payload
     * @return a validation-failed CommonResult
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

/**
     * Returns a failed response indicating validation failure with a custom message.
     *
     * @param message the custom validation failure message
     * @param <T> the type of the payload
     * @return a validation-failed CommonResult
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

/**
     * Returns an unauthorized response, typically when authentication fails.
     *
     * @param data the payload data or context for the unauthorized access
     * @param <T> the type of the payload
     * @return an unauthorized CommonResult
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

/**
     * Returns a forbidden response, typically when access is denied due to permissions.
     *
     * @param data the payload data or context for the forbidden access
     * @param <T> the type of the payload
     * @return a forbidden CommonResult
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
