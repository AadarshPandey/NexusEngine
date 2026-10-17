package com.nexusengine.core.common.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLSyntaxErrorException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (e.getErrorCode() != null && e.getErrorCode().getCode() != 0) {
            try {
                status = HttpStatus.valueOf((int) e.getErrorCode().getCode());
            } catch (IllegalArgumentException ex) {
                status = HttpStatus.BAD_REQUEST;
            }
        }
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        if (e.getErrorCode() != null) {
            pd.setTitle("API Error");
        }
        return pd;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Resource Not Found");
        pd.setProperty("resource", ex.getResourceType());
        if (ex.getIdentifier() != null) {
            pd.setProperty("identifier", ex.getIdentifier());
        }
        return pd;
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(
            org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Endpoint Not Found");
        return pd;
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(InsufficientStockException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Insufficient Stock");
        if (ex.getSkuId() != null) {
            pd.setProperty("skuId", ex.getSkuId());
        }
        return pd;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ProblemDetail handleValidationException(Exception e) {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException manve) {
            bindingResult = manve.getBindingResult();
        } else if (e instanceof BindException be) {
            bindingResult = be.getBindingResult();
        }

        String message = "Validation failed";
        if (bindingResult != null && bindingResult.hasErrors()) {
            message =
                    bindingResult.getFieldErrors().stream()
                            .map(f -> f.getField() + " " + f.getDefaultMessage())
                            .collect(Collectors.joining(", "));
        }
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        pd.setTitle("Validation Error");
        if (bindingResult != null && bindingResult.hasErrors()) {
            java.util.Map<String, String> fieldErrors =
                    bindingResult.getFieldErrors().stream()
                            .collect(
                                    Collectors.toMap(
                                            org.springframework.validation.FieldError::getField,
                                            f ->
                                                    f.getDefaultMessage() != null
                                                            ? f.getDefaultMessage()
                                                            : "invalid",
                                            (a, b) -> a));
            pd.setProperty("fieldErrors", fieldErrors);
        }
        return pd;
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ProblemDetail handleSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred");
        pd.setTitle("Database Error");
        return pd;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception e) {
        log.error("Unhandled exception caught by GlobalExceptionHandler", e);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred. Please contact support.");
        pd.setTitle("Internal Server Error");
        return pd;
    }
}
