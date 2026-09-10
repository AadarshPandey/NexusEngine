package com.nexusengine.core.security.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Validates that the JWT secret is properly configured at startup.
 * Prevents the application from running with a weak or missing secret.
 */
@Component
public class JwtSecretValidator {

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET environment variable must be set");
        }
        if (secret.length() < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must be at least 32 characters for HS512 security. " +
                    "Generate one with: openssl rand -base64 48");
        }
    }
}
