package com.ticketjo.ticketjo_backend.config;


import com.ticketjo.ticketjo_backend.security.JwtAuthenticationFilter;
import com.ticketjo.ticketjo_backend.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Définit le bean CorsConfigurationSource que Spring (MVC + Security) utilisera.
     */
    @Bean("securityCorsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // En DEV : autoriser localhost:5173 "http://localhost:5173",
        // En PROD : fbah-ticketjo.fr et www.fbah-ticketjo.fr
        cfg.setAllowedOriginPatterns(List.of( "https://fbah-ticketjo.fr", "https://www.fbah-ticketjo.fr" ));
        // En dev, possible faire cfg.addAllowedOriginPattern("*");  
        cfg.setAllowedOrigins(List.of("https://fbah-ticketjo.fr", "https://www.fbah-ticketjo.fr"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1) Active CORS via notre CorsConfigurationSource
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 2) Désactive CSRF, passe en stateless, règle les accès
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Routes publiques
                .requestMatchers(
                	"/oauth2/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/uploads/**",
                    "/api/auth/**",
                    "/api/stripe/**", 
                    "/"
                ).permitAll()
                // Rôles spécifiques
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/evenements/upload").hasRole("ADMIN")
                .requestMatchers("/api/evenements/**").hasRole("ADMIN")
                // Autorise les pré‐vols CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Tout le reste requiert authentification
                .anyRequest().authenticated()
            )
            // 3) Authentification par DAO + BCrypt
            .authenticationProvider(authenticationProvider())
            // 4) Filtre JWT
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        	// 6) Activation OAuth2 login (Google, Apple…)
        	.oauth2Login(withDefaults());

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider();
        prov.setUserDetailsService(customUserDetailsService);
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
