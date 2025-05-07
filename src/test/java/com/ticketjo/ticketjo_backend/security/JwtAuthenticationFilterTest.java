package com.ticketjo.ticketjo_backend.security;

import com.ticketjo.ticketjo_backend.service.impl.CustomUserDetailsService;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Test
    void doFilterInternal_ShouldAuthenticate_WhenTokenIsValid() throws Exception {
        // Arrange
        String token = "Bearer valid-token";
        String email = "user@example.com";

        UserDetails userDetails = new User(email, "password", Collections.emptyList());

        when(jwtUtil.extractUsername("valid-token")).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtil.isTokenValid("valid-token", userDetails)).thenReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        var authentication = (UsernamePasswordAuthenticationToken)
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
        assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    void doFilterInternal_ShouldSkipAuthentication_WhenHeaderIsMissing() throws IOException, ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = mock(MockFilterChain.class);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}