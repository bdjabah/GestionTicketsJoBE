package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implémentation personnalisée de UserDetailsService pour Spring Security.
 * Charge un utilisateur par email et retourne ses détails pour l'authentification.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        // Convertit le rôle en autorité Spring Security (ROLE_...)
        String roleName = utilisateur.getRole().getNomRole();
        return new org.springframework.security.core.userdetails.User(
                utilisateur.getEmail(),
                utilisateur.getMotDePasse(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }
}