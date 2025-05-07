package com.ticketjo.ticketjo_backend.config;

import com.ticketjo.ticketjo_backend.security.JwtAuthenticationFilter;
import com.ticketjo.ticketjo_backend.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(jwtAuthenticationFilter, customUserDetailsService);
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
        String rawPassword = "password123";
        String encoded = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(result);
        assertEquals(mockManager, result);
    }
}
