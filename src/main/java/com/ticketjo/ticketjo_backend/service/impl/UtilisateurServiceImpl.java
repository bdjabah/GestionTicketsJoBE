package com.ticketjo.ticketjo_backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.repository.UtilisateurRepository;
import com.ticketjo.ticketjo_backend.service.UtilisateurService;

import lombok.RequiredArgsConstructor;

/**
 * Implémentation de l'interface UtilisateurService.
 * Contient la logique métier pour la gestion des utilisateurs.
 */
@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    // Dépendance injectée automatiquement grâce à Lombok
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Crée un nouvel utilisateur avec une clé UUID unique
     * et la date d'inscription à aujourd'hui.
     *
     * @param utilisateur L'utilisateur à enregistrer
     * @return L'utilisateur persisté
     */
    @Override
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Un compte avec cet email existe déjà.");
        }
        utilisateur.setCleUtilisateur(UUID.randomUUID().toString());
        utilisateur.setDateInscription(LocalDate.now());
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Supprime un utilisateur selon son ID.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur
     */
    @Override
    public void deleteUtilisateur(Long idUtilisateur) {
        utilisateurRepository.deleteById(idUtilisateur);
    }

    /**
     * Cherche un utilisateur par son email.
     *
     * @param email L'adresse email à chercher
     * @return Un Optional avec l'utilisateur, ou vide
     */
    @Override
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return Liste des utilisateurs
     */
    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
}
