package com.nexusengine.core.portal.component;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.service.UmsMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private UmsMemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (path.contains("/portal/product/list") || path.contains("/sso/login")) {
            String clientIp = getClientIP(request);
            RRateLimiter rateLimiter = redissonClient.getRateLimiter("rate_limit:ip:" + clientIp);
            // 100 requests per minute
            rateLimiter.trySetRate(RateType.OVERALL, 100, 1, RateIntervalUnit.MINUTES);
            
            if (!rateLimiter.tryAcquire(1)) {
                handleRateLimitResponse(response, 60);
                return false;
            }
        } else if (path.contains("/portal/order/createRazorpayOrder") || path.contains("/portal/order/verifyRazorpayPayment")) {
            UmsMember currentMember = null;
            try {
                currentMember = memberService.getCurrentMember();
            } catch (Exception e) {
                // Not logged in or token invalid
            }
            
            if (currentMember != null) {
                String username = currentMember.getUsername();
                RRateLimiter rateLimiter = redissonClient.getRateLimiter("rate_limit:user:" + username);
                // 10 requests per minute
                rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);
                
                if (!rateLimiter.tryAcquire(1)) {
                    handleRateLimitResponse(response, 60);
                    return false;
                }
            }
        }
        
        return true;
    }



    private void handleRateLimitResponse(HttpServletResponse response, long retryAfterSeconds) throws Exception {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        response.setContentType("application/json;charset=UTF-8");
        
        CommonResult<Object> result = CommonResult.failed("Too Many Requests. Please try again later.");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
