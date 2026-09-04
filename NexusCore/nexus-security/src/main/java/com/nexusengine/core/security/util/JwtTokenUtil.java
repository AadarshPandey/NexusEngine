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
 * Auto-generated documentation
 * Auto-generated documentation
 * Auto-generated documentation
 * {"alg": "HS512","typ": "JWT"}
 * Auto-generated documentation
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * Auto-generated documentation
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by macro on 2018/4/26.
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
     * Auto-generated documentation
     */
    private byte[] getSigningKey() {
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Auto-generated documentation
     */
    private String generateToken(Map<String, Object> claims) {
        // Auto-generated documentation
        long expireTime = System.currentTimeMillis() + expiration * 1000;
        claims.put("exp", expireTime);
        return JWTUtil.createToken(claims, getSigningKey());
    }

    /**
     * Auto-generated documentation
     */
    private Map<String, Object> getPayloadFromToken(String token) {
        try {
            // Auto-generated documentation
            if (!JWTUtil.verify(token, getSigningKey())) {
                LOGGER.info("Success", token);
                return null;
            }
            // Auto-generated documentation
            return JWTUtil.parseToken(token).getPayloads();
        } catch (Exception e) {
            LOGGER.info("Success", token);
            return null;
        }
    }

    /**
     * Auto-generated documentation
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
     * Auto-generated documentation
     *
     * Auto-generated documentation
     * Auto-generated documentation
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Auto-generated documentation
     */
    private boolean isTokenExpired(String token) {
        try {
            // Auto-generated documentation
            Map<String, Object> payload = getPayloadFromToken(token);
            if (payload == null) {
                return true;
            }
            Object exp = payload.get("exp");
            if (exp == null) {
                return false;
            }
            long expTime = exp instanceof Long ? (Long) exp : ((Number) exp).longValue();
            return expTime < System.currentTimeMillis();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Auto-generated documentation
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
     * Auto-generated documentation
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * Auto-generated documentation
     *
     * Auto-generated documentation
     */
    public String refreshHeadToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        // Auto-generated documentation
        Map<String, Object> payload = getPayloadFromToken(token);
        if (payload == null) {
            return null;
        }
        // Auto-generated documentation
        if (isTokenExpired(token)) {
            return null;
        }
        // Auto-generated documentation
        if (tokenRefreshJustBefore(token, 30 * 60)) {
            return token;
        } else {
            payload.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(payload);
        }
    }

    /**
     * Auto-generated documentation
     *
     * Auto-generated documentation
     * Auto-generated documentation
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
        // Auto-generated documentation
        return refreshDate.after(createdDate) && refreshDate.before(DateUtil.offsetSecond(createdDate, time));
    }
}
