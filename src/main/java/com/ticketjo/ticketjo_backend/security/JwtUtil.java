package com.ticketjo.ticketjo_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilitaire pour gérer les JWT : génération, validation et extraction de claims.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24h

    /**
     * Génère un JWT à partir des informations d'un utilisateur.
     * <p>
     * Le token contiendra le sujet (username) et un claim "roles" listant les autorités Spring.
     * </p>
     *
     * @param userDetails les détails de l'utilisateur (username et rôles)
     * @return le JWT signé en HS256
     */
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Génère un JWT minimaliste à partir d'un email.
     * <p>
     * Utile pour compatibilité ou cas simple où l'on ne gère pas les rôles.
     * </p>
     *
     * @param email l'email à placer en tant que sujet du token
     * @return le JWT signé en HS256
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur (subject) depuis un JWT.
     *
     * @param token le JWT à analyser
     * @return le nom d'utilisateur (subject) contenu dans le token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait l'email (identique au subject) depuis un JWT.
     *
     * @param token le JWT à analyser
     * @return l'email (subject) contenu dans le token
     */
    public String extractEmail(String token) {
        return extractUsername(token);
    }

    /**
     * Vérifie si un token est valide pour l'utilisateur donné.
     * <p>
     * Vérifie que le subject correspond et que le token n'est pas expiré.
     * </p>
     *
     * @param token       le JWT à vérifier
     * @param userDetails les détails de l'utilisateur attendu
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extrait la date d'expiration d'un JWT.
     *
     * @param token le JWT à analyser
     * @return la date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Vérifie si un token est expiré.
     *
     * @param token le JWT à analyser
     * @return {@code true} si la date d'expiration est antérieure à la date courante
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait un claim spécifique depuis un JWT à l'aide d'un resolver.
     *
     * @param token    le JWT à analyser
     * @param resolver fonction pour extraire le claim désiré depuis l'objet Claims
     * @param <T>      le type du claim retourné
     * @return la valeur du claim extrait
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    /**
     * Récupère tous les claims depuis un JWT signé.
     *
     * @param token le JWT à analyser
     * @return l'objet Claims contenant toutes les informations du token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Construit la clé de signature HMAC à partir du secret configuré.
     *
     * @return la clé {@link Key} pour signer ou vérifier les JWT
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}


/*
 * import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.stereotype.Component;
 * 
 * import java.security.Key; import java.util.Date; import
 * java.util.function.Function;
 * 
 *//**
	 * Utilitaire pour gérer les JWT : génération, validation, extraction.
	 */
/*
 * @Component public class JwtUtil {
 * 
 * @Value("${jwt.secret}") private String secret;
 * 
 * private final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24h
 * 
 *//**
	 * Génère un JWT à partir de l'email utilisateur.
	 */
/*
 * public String generateToken(String email) { return Jwts.builder()
 * .setSubject(email) .setIssuedAt(new Date()) .setExpiration(new
 * Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
 * .signWith(getSigningKey(), SignatureAlgorithm.HS256) .compact(); }
 * 
 *//**
	 * Extrait le nom d'utilisateur (email) du token.
	 */
/*
 * public String extractUsername(String token) { return extractClaim(token,
 * Claims::getSubject); }
 *//**
	 * Récupère l'email (identité de l'utilisateur) depuis un token JWT.
	 *
	 * @param token Le token JWT à analyser.
	 * @return L'email extrait du token.
	 */
/*
 * public String extractEmail(String token) { return extractUsername(token); }
 *//**
	 * Vérifie si le token est valide pour l'utilisateur donné.
	 */
/*
 * public boolean isTokenValid(String token, UserDetails userDetails) { String
 * username = extractUsername(token); return
 * username.equals(userDetails.getUsername()) && !isTokenExpired(token); }
 * 
 *//**
	 * Vérifie si le token a expiré.
	 */
/*
 * private boolean isTokenExpired(String token) { return
 * extractExpiration(token).before(new Date()); }
 * 
 *//**
	 * Extrait la date d’expiration du token.
	 */
/*
 * public Date extractExpiration(String token) { return extractClaim(token,
 * Claims::getExpiration); }
 * 
 *//**
	 * Méthode générique pour extraire un claim spécifique.
	 */
/*
 * public <T> T extractClaim(String token, Function<Claims, T> resolver) {
 * return resolver.apply(extractAllClaims(token)); }
 * 
 *//**
	 * Parse tous les claims présents dans le JWT.
	 */
/*
 * private Claims extractAllClaims(String token) { return Jwts.parserBuilder()
 * .setSigningKey(getSigningKey()) .build() .parseClaimsJws(token) .getBody(); }
 * 
 *//**
	 * Construit une clé de signature HMAC à partir du secret.
	 *//*
		 * private Key getSigningKey() { return Keys.hmacShaKeyFor(secret.getBytes()); }
		 * }
		 */