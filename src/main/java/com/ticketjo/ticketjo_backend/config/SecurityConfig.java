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
     // En DEV : autoriser localhost:5173
        // En PROD : fbah-ticketjo.fr et www.fbah-ticketjo.fr
        cfg.setAllowedOriginPatterns(List.of(
            "http://localhost:5173",
            "https://fbah-ticketjo.fr",
            "https://www.fbah-ticketjo.fr"
        ));
        // En dev, vous pouvez faire cfg.addAllowedOriginPattern("*");
        //cfg.setAllowedOrigins(List.of("https://fbah-ticketjo.fr", "https://www.fbah-ticketjo.fr"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
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

/*Ancienne classe 
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
*//*
/**
 * Configuration principale de Spring Security.
 * Gère les accès, l'intégration de JWT, et le hachage des mots de passe.
 *//*
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
 /*   @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

        // ✅ Utilise ceci à la place
        .cors(cors -> {}) // OU simplement : .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger accessible sans authentification
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/uploads/**"
                ).permitAll()

                // Auth endpoints libres
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/stripe/**").permitAll()

                // Rôles = accès restreint
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/evenements/upload").hasRole("ADMIN")
                .requestMatchers("/api/evenements/**").hasRole("ADMIN")

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
/*    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Fournit un AuthenticationManager pour gérer l’authentification manuelle si besoin.
     */
/*    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean de hachage des mots de passe avec BCrypt.
     * Obligatoire pour faire semblant d’être sécurisé.
     */
/*    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}*/