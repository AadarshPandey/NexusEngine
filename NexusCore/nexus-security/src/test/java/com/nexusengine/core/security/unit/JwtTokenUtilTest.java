package com.nexusengine.core.security.unit;

import com.nexusengine.core.security.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "test-jwt-secret-key-that-is-at-least-32-characters-long-for-hs512");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 3600L);
        ReflectionTestUtils.setField(jwtTokenUtil, "tokenHead", "Bearer ");

        userDetails = User.withUsername("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void generateToken_ReturnsValidJwt() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void getUsernameFromToken_ReturnsCorrectUsername() {
        String token = jwtTokenUtil.generateToken(userDetails);
        String username = jwtTokenUtil.getUserNameFromToken(token);
        assertEquals("member:testuser", username);
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void validateToken_TamperedToken_ReturnsFalse() {
        String token = jwtTokenUtil.generateToken(userDetails);
        String tamperedToken = token.substring(0, token.length() - 5) + "abcde";
        assertFalse(jwtTokenUtil.validateToken(tamperedToken, userDetails));
    }
}
