package com.nexusengine.core.security.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.nexusengine.core.security.config.SecurityConfig;
import com.nexusengine.core.security.service.JwtTokenService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.util.ReflectionTestUtils;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private JwtDecoder jwtDecoder;
    private JwtEncoder jwtEncoder;
    private JwtTokenService jwtTokenService;

    private static final String SECRET = "012345678901234567890123456789012345678901234567";

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        jwtDecoder = securityConfig.jwtDecoder(SECRET);
        jwtEncoder = securityConfig.jwtEncoder(SECRET);

        jwtTokenService = new JwtTokenService(jwtEncoder, jwtDecoder);
        ReflectionTestUtils.setField(jwtTokenService, "expirationSeconds", 3600L);
        ReflectionTestUtils.setField(jwtTokenService, "tokenHead", "Bearer ");
    }

    @Test
    void generateAndDecodeToken_ValidToken_ReturnsCorrectSubjectAndClaims() {
        String token = jwtTokenService.generateToken("testadmin", List.of("ROLE_ADMIN", "product:read"));
        assertNotNull(token);
        assertFalse(token.isBlank());

        Jwt jwt = jwtTokenService.decodeToken("Bearer " + token);
        assertNotNull(jwt);
        assertEquals("testadmin", jwt.getSubject());
        assertEquals("https://nexusengine.com", jwt.getIssuer().toString());

        List<String> roles = jwt.getClaimAsStringList("roles");
        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_ADMIN"));
        assertTrue(roles.contains("product:read"));
        assertEquals("ADMIN", jwt.getClaimAsString("typ"));
        assertEquals("testadmin", jwt.getClaimAsString("username"));
        assertEquals(roles, jwt.getClaimAsStringList("authorities"));

        String username = jwtTokenService.getUsernameFromToken(token);
        assertEquals("testadmin", username);
    }

    @Test
    void refreshToken_ValidToken_ReturnsNewValidToken() {
        String oldToken = jwtTokenService.generateToken("testuser", List.of("ROLE_CUSTOMER"));
        String refreshedToken = jwtTokenService.refreshHeadToken(oldToken);

        assertNotNull(refreshedToken);
        assertEquals("testuser", jwtTokenService.getUsernameFromToken(refreshedToken));
    }

    @Test
    void decodeToken_InvalidSignature_ReturnsNull() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.signature";
        assertNull(jwtTokenService.decodeToken(invalidToken));
    }

    @Test
    void jwtDecoder_TooShortSecret_ThrowsException() {
        assertThrows(IllegalStateException.class, () -> securityConfig.jwtDecoder("short"));
    }
}
