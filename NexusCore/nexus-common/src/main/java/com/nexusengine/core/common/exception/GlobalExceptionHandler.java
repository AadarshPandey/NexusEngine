package com.nexusengine.core.common.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.common.api.CommonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLSyntaxErrorException;

/**
 * Represents the GlobalExceptionHandler component.
 * Provides core functionality and operations for GlobalExceptionHandler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = ApiException.class)
    public org.springframework.http.ResponseEntity<CommonResult> handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(CommonResult.failed(e.getErrorCode()));
        }
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(CommonResult.failed(e.getMessage()));
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<CommonResult> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(CommonResult.validateFailed(message));
    }


    @ExceptionHandler(value = BindException.class)
    public org.springframework.http.ResponseEntity<CommonResult> handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(CommonResult.validateFailed(message));
    }


    @ExceptionHandler(value = SQLSyntaxErrorException.class)
    public org.springframework.http.ResponseEntity<CommonResult> handleSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResult.failed("Database error occurred"));
    }
}
