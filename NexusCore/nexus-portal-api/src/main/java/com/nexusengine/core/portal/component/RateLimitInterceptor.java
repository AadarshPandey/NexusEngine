package com.nexusengine.core.portal.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.portal.customer.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private final RedissonClient redissonClient;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    public RateLimitInterceptor(
            RedissonClient redissonClient,
            MemberService memberService,
            ObjectMapper objectMapper) {
        this.redissonClient = redissonClient;
        this.memberService = memberService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();

        if (path.contains("/portal/product/list") || path.contains("/sso/login")) {
            String clientIp = getClientIP(request);
            RRateLimiter rateLimiter = redissonClient.getRateLimiter("rate_limit:ip:" + clientIp);
            rateLimiter.trySetRate(RateType.OVERALL, 100, 1, RateIntervalUnit.MINUTES);

            if (!rateLimiter.tryAcquire(1)) {
                handleRateLimitResponse(response, 60);
                return false;
            }
        } else if (path.contains("/portal/order/createRazorpayOrder")
                || path.contains("/portal/order/verifyRazorpayPayment")) {
            Member currentMember = null;
            try {
                currentMember = memberService.getCurrentMember();
            } catch (Exception e) {
                log.debug("Could not resolve current member for rate limiting: {}", e.getMessage());
            }

            if (currentMember != null) {
                String username = currentMember.getUsername();
                RRateLimiter rateLimiter =
                        redissonClient.getRateLimiter("rate_limit:user:" + username);
                rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);

                if (!rateLimiter.tryAcquire(1)) {
                    handleRateLimitResponse(response, 60);
                    return false;
                }
            }
        }

        return true;
    }

    private void handleRateLimitResponse(HttpServletResponse response, long retryAfterSeconds)
            throws Exception {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        response.setContentType("application/problem+json;charset=UTF-8");

        ProblemDetail pd =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Too Many Requests. Please try again later.");
        pd.setTitle("Rate Limit Exceeded");
        response.getWriter().write(objectMapper.writeValueAsString(pd));
    }

    private String getClientIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
