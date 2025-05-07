package com.ticketjo.ticketjo_backend.security;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "12345678901234567890123456789012"; // 32 chars for HMAC-SHA256

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
    }

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@example.com";
        UserDetails userDetails = User.withUsername(email).password("dummy").authorities("ROLE_USER").build();

        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    void testExtractUsername() {
        String email = "testuser@example.com";
        String token = jwtUtil.generateToken(email);

        String extracted = jwtUtil.extractUsername(token);
        assertEquals(email, extracted);
    }

    @Test
    void testTokenExpiration() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        Date expiration = jwtUtil.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testExtractAllClaims() {
        String email = "claims@example.com";
        String token = jwtUtil.generateToken(email);

        Claims claims = ReflectionTestUtils.invokeMethod(jwtUtil, "extractAllClaims", token);
        assertNotNull(claims);
        assertEquals(email, claims.getSubject());
    }
}
