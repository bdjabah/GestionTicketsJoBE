package com.ticketjo.ticketjo_backend.controller;


import com.ticketjo.ticketjo_backend.dto.LoginDTO;
import com.ticketjo.ticketjo_backend.dto.UtilisateurDTO;
import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.service.RoleService;
import com.ticketjo.ticketjo_backend.mapper.UtilisateurMapper;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.security.JwtUtil;
import com.ticketjo.ticketjo_backend.service.UtilisateurService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur responsable de l'authentification utilisateur.
 * Gère l'enregistrement et la connexion.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;

    /**
     * Enregistre un nouvel utilisateur avec un rôle USER par défaut.
     * Le mot de passe est encodé avant sauvegarde (parce qu’on n’est pas des criminels).
     *
     * @param utilisateurDTO les infos de l’utilisateur à créer.
     * @return DTO de l’utilisateur enregistré.
     */
    @PostMapping("/register")
    public ResponseEntity<UtilisateurDTO> register(@RequestBody @Valid UtilisateurDTO utilisateurDTO) {
        // Hachage du mot de passe
        utilisateurDTO.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));

        // Récupération complète du rôle depuis la BDD
        Role roleUser = roleService.trouverRoleParNom("USER"); // 🧠 Correspond exactement à la BDD
        Utilisateur utilisateur = UtilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setRole(roleUser); // ⛔ Ne pas mapper partiellement !

        // Création
        Utilisateur created = utilisateurService.createUtilisateur(utilisateur);
        return new ResponseEntity<>(UtilisateurMapper.toDTO(created), HttpStatus.CREATED);
    }

    /**
     * Authentifie un utilisateur à partir de son email et mot de passe.
     * Si l'authentification réussit, un token JWT est généré et retourné.
     *
     * @param loginDTO Données d’identification : email + mot de passe.
     * @return token JWT et infos utiles.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        // Récupérer l’utilisateur à partir de l’email
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec cet email"));

        // Vérification du mot de passe
        if (!passwordEncoder.matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
        }

        // Générer un faux JWT (ou un vrai si t’as une vraie implémentation)
        String token = jwtUtil.generateToken(utilisateur.getEmail());

        // Réponse contenant le token et quelques infos
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", utilisateur.getEmail());
        response.put("role", utilisateur.getRole().getNomRole());

        return ResponseEntity.ok(response);
    }
}