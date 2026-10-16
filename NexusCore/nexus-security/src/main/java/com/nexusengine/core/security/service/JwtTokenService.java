package com.nexusengine.core.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * Service generating and validating JWT tokens using Spring OAuth2 NimbusJwtEncoder/Decoder.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.expiration:604800}")
    private long expirationSeconds;

    @Value("${jwt.tokenHead:Bearer }")
    private String tokenHead;

    public String generateToken(UserDetails userDetails) {
        List<String> roles =
                userDetails.getAuthorities() != null
                        ? userDetails.getAuthorities().stream().map(Object::toString).toList()
                        : Collections.emptyList();
        return generateToken(userDetails.getUsername(), roles);
    }

    public String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        String userType =
                roles != null && roles.stream().anyMatch(r -> r.contains("ADMIN"))
                        ? "ADMIN"
                        : "CUSTOMER";
        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer("https://nexusengine.com")
                        .issuedAt(now)
                        .expiresAt(now.plus(expirationSeconds, ChronoUnit.SECONDS))
                        .subject(username)
                        .claim("username", username)
                        .claim("typ", userType)
                        .claim("roles", roles != null ? roles : Collections.emptyList())
                        .claim("authorities", roles != null ? roles : Collections.emptyList())
                        .claim("scope", roles != null ? String.join(" ", roles) : "")
                        .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String getUsernameFromToken(String token) {
        Jwt jwt = decodeToken(token);
        return jwt != null ? jwt.getSubject() : null;
    }

    public Jwt decodeToken(String token) {
        try {
            if (token != null && token.startsWith(tokenHead.trim())) {
                token = token.substring(tokenHead.length()).trim();
            }
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    public String refreshHeadToken(String oldToken) {
        if (oldToken == null || oldToken.isBlank()) {
            return null;
        }
        Jwt jwt = decodeToken(oldToken);
        if (jwt == null) {
            return null;
        }
        String username = jwt.getSubject();
        List<String> roles = jwt.getClaimAsStringList("roles");
        return generateToken(username, roles);
    }
}
