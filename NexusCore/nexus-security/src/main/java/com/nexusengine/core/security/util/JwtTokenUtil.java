package com.nexusengine.core.security.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * {"alg": "HS512","typ": "JWT"}
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Refactored to use Hutool JWTUtil
 */
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    private byte[] getSigningKey() {
        return secret.getBytes(StandardCharsets.UTF_8);
    }

        /**
     * Executes the operation.
     * @param claims the claims
     * @return the result of the operation
     */
    private String generateToken(Map<String, Object> claims) {
        // Fix 8: Standard JWT exp claim must be in seconds since epoch, not milliseconds.
        long expireTime = (System.currentTimeMillis() / 1000) + expiration;
        claims.put("exp", expireTime);
        return JWTUtil.createToken(claims, getSigningKey());
    }

        /**
     * Executes the operation.
     * @param token the token
     * @return the result of the operation
     */
    private Map<String, Object> getPayloadFromToken(String token) {
        try {
            if (!JWTUtil.verify(token, getSigningKey())) {
                LOGGER.debug("JWT token signature verification failed");
                return null;
            }
            return JWTUtil.parseToken(token).getPayloads();
        } catch (Exception e) {
            LOGGER.debug("JWT token verification failed", e);
            return null;
        }
    }

        /**
     * Executes the operation.
     * @param token the token
     * @return the result of the operation
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Map<String, Object> payload = getPayloadFromToken(token);
            username = payload != null ? (String) payload.get(CLAIM_KEY_USERNAME) : null;
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     *
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        if (username != null && username.contains(":")) {
            username = username.substring(username.indexOf(":") + 1);
        }
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

        /**
     * Executes the operation.
     * @param token the token
     * @return the result of the operation
     */
    private boolean isTokenExpired(String token) {
        try {
            Map<String, Object> payload = getPayloadFromToken(token);
            if (payload == null) {
                return true;
            }
            Object exp = payload.get("exp");
            if (exp == null) {
                return false;
            }
            long expTime = exp instanceof Long ? (Long) exp : ((Number) exp).longValue();
            return expTime < (System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            return true;
        }
    }

        /**
     * Executes the operation.
     * @param token the token
     * @return the result of the operation
     */
    private Date getExpiredDateFromToken(String token) {
        Map<String, Object> payload = getPayloadFromToken(token);
        if (payload == null) {
            return null;
        }
        Object exp = payload.get("exp");
        if (exp instanceof Long) {
            return new Date((Long) exp);
        } else if (exp instanceof Integer) {
            return new Date(((Integer) exp).longValue());
        }
        return null;
    }

        /**
     * Executes the operation.
     * @param userDetails the userDetails
     * @return the result of the operation
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String prefix = userDetails.getClass().getSimpleName().contains("Admin") ? "admin:" : "member:";
        claims.put(CLAIM_KEY_USERNAME, prefix + userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     *
     */
    public String refreshHeadToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        Map<String, Object> payload = getPayloadFromToken(token);
        if (payload == null) {
            return null;
        }
        if (isTokenExpired(token)) {
            return null;
        }
        if (tokenRefreshJustBefore(token, 30 * 60)) {
            return token;
        } else {
            payload.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(payload);
        }
    }

    /**
     *
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Map<String, Object> payload = getPayloadFromToken(token);
        if (payload == null) {
            return false;
        }
        Object created = payload.get(CLAIM_KEY_CREATED);
        Date createdDate = null;
        if (created instanceof Long) {
            createdDate = new Date((Long) created);
        } else if (created instanceof Date) {
            createdDate = (Date) created;
        }
        if (createdDate == null) {
            return false;
        }
        Date refreshDate = new Date();
        return refreshDate.after(createdDate) && refreshDate.before(DateUtil.offsetSecond(createdDate, time));
    }
}
