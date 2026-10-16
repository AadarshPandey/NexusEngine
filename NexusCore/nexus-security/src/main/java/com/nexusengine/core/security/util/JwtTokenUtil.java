package com.nexusengine.core.security.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT utility using standard JDK HmacSHA512 and Jackson ObjectMapper.
 */
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String HMAC_ALGO = "HmacSHA512";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String HEADER_B64 = Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"alg\":\"HS512\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private byte[] getSigningKey() {
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    private String sign(String content) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(getSigningKey(), HMAC_ALGO));
        byte[] sig = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(sig);
    }

    private String generateToken(Map<String, Object> claims) {
        try {
            long expireTime = Instant.now().getEpochSecond() + expiration;
            claims.put("exp", expireTime);
            String payloadJson = OBJECT_MAPPER.writeValueAsString(claims);
            String payloadB64 = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
            String signingInput = HEADER_B64 + "." + payloadB64;
            return signingInput + "." + sign(signingInput);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate JWT token", e);
        }
    }

    private Map<String, Object> getPayloadFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            String signingInput = parts[0] + "." + parts[1];
            String expectedSig = sign(signingInput);
            if (!MessageDigest.isEqual(
                    expectedSig.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8))) {
                LOGGER.debug("JWT token signature verification failed");
                return null;
            }
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            return OBJECT_MAPPER.readValue(payloadBytes, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            LOGGER.debug("JWT token verification failed", e);
            return null;
        }
    }

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

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        if (username != null && username.contains(":")) {
            username = username.substring(username.indexOf(":") + 1);
        }
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

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
            return expTime < Instant.now().getEpochSecond();
        } catch (Exception e) {
            return true;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String prefix = userDetails.getClass().getSimpleName().contains("Admin") ? "admin:" : "member:";
        claims.put(CLAIM_KEY_USERNAME, prefix + userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, Instant.now().toEpochMilli());
        return generateToken(claims);
    }

    public String refreshHeadToken(String oldToken) {
        if (!StringUtils.hasText(oldToken) || !oldToken.startsWith(tokenHead)) {
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if (!StringUtils.hasText(token)) {
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
            payload.put(CLAIM_KEY_CREATED, Instant.now().toEpochMilli());
            return generateToken(payload);
        }
    }

    private boolean tokenRefreshJustBefore(String token, int time) {
        Map<String, Object> payload = getPayloadFromToken(token);
        if (payload == null) {
            return false;
        }
        Object created = payload.get(CLAIM_KEY_CREATED);
        Instant createdInstant = null;
        if (created instanceof Long) {
            createdInstant = Instant.ofEpochMilli((Long) created);
        } else if (created instanceof Number) {
            createdInstant = Instant.ofEpochMilli(((Number) created).longValue());
        }
        if (createdInstant == null) {
            return false;
        }
        Instant now = Instant.now();
        return now.isAfter(createdInstant) && now.isBefore(createdInstant.plusSeconds(time));
    }
}
