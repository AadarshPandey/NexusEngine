package com.nexusengine.core.common.exception;

import com.nexusengine.core.common.api.ResultCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.sql.SQLSyntaxErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Collections;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleApiException() {
        ApiException ex = new ApiException(ResultCode.UNAUTHORIZED);
        ProblemDetail pd = handler.handleApiException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), pd.getStatus());
        assertEquals("API Error", pd.getTitle());
    }

    @Test
    void handleValidationException() {
        BindException ex = mock(BindException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("object", "field", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ProblemDetail pd = handler.handleValidationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST.value(), pd.getStatus());
        assertEquals("Validation Error", pd.getTitle());
        assertEquals("field must not be null", pd.getDetail());
    }

    @Test
    void handleSQLSyntaxErrorException() {
        SQLSyntaxErrorException ex = new SQLSyntaxErrorException("Syntax error");
        ProblemDetail pd = handler.handleSQLSyntaxErrorException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), pd.getStatus());
        assertEquals("Database Error", pd.getTitle());
        assertEquals("Database error occurred", pd.getDetail());
    }

    @Test
    void handleGlobalException() {
        Exception ex = new Exception("Some internal error");
        ProblemDetail pd = handler.handleGlobalException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), pd.getStatus());
        assertEquals("Internal Server Error", pd.getTitle());
        assertEquals("An unexpected internal error occurred. Please contact support.", pd.getDetail());
    }
}
