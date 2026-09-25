package com.nexusengine.core.portal.unit.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.component.RateLimitInterceptor;
import com.nexusengine.core.portal.service.UmsMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RateLimitInterceptorTest {

    @Mock
    private RedissonClient redissonClient;
    @Mock
    private UmsMemberService memberService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RRateLimiter rateLimiter;

    @InjectMocks
    private RateLimitInterceptor interceptor;

    @BeforeEach
    void setUp() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
    }

    @Test
    void preHandle_ProductListPath_IPRateLimited_Returns429() throws Exception {
        when(request.getRequestURI()).thenReturn("/portal/product/list");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(redissonClient.getRateLimiter(anyString())).thenReturn(rateLimiter);
        when(rateLimiter.tryAcquire(1)).thenReturn(false);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(429);
        verify(rateLimiter).trySetRate(RateType.OVERALL, 100, 1, RateIntervalUnit.MINUTES);
    }

    @Test
    void preHandle_PaymentPath_UserRateLimited_Returns429() throws Exception {
        when(request.getRequestURI()).thenReturn("/portal/order/createRazorpayOrder");
        UmsMember member = new UmsMember();
        member.setUsername("testuser");
        when(memberService.getCurrentMember()).thenReturn(member);
        when(redissonClient.getRateLimiter("rate_limit:user:testuser")).thenReturn(rateLimiter);
        when(rateLimiter.tryAcquire(1)).thenReturn(false);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(429);
        verify(rateLimiter).trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);
    }

    @Test
    void preHandle_NormalPath_NoRateLimit_Proceeds() throws Exception {
        when(request.getRequestURI()).thenReturn("/portal/home/content");
        
        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(redissonClient, never()).getRateLimiter(anyString());
    }

    @Test
    void preHandle_PaymentPath_UnauthenticatedUser_Proceeds() throws Exception {
        when(request.getRequestURI()).thenReturn("/portal/order/verifyRazorpayPayment");
        when(memberService.getCurrentMember()).thenThrow(new RuntimeException("Not authenticated"));

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(redissonClient, never()).getRateLimiter(anyString());
    }
}
