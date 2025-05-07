package com.ticketjo.ticketjo_backend.config;

import com.ticketjo.ticketjo_backend.security.JwtAuthenticationFilter;
import com.ticketjo.ticketjo_backend.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration principale de Spring Security.
 * Gère les accès, l'intégration de JWT, et le hachage des mots de passe.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Définit les règles de sécurité de l'application.
     * - Désactive CSRF
     * - Active la sécurité par token (stateless)
     * - Autorise certaines routes publiques
     * - Protège les autres routes
     * - Injecte le filtre JWT personnalisé
     */
    @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger accessible sans authentification
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/webjars/**"
                ).permitAll()

                // Auth endpoints libres
                .requestMatchers("/api/auth/**").permitAll()

                // Rôles = accès restreint
                .requestMatchers("/api/roles/**").hasRole("ADMIN")

                // Toute autre route nécessite un token
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Fournit un AuthenticationProvider basé sur ton CustomUserDetailsService.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Fournit un AuthenticationManager pour gérer l’authentification manuelle si besoin.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean de hachage des mots de passe avec BCrypt.
     * Obligatoire pour faire semblant d’être sécurisé.
     */
    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}