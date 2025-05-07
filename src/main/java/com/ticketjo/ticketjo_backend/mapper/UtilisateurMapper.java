package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.UtilisateurDTO;
import com.ticketjo.ticketjo_backend.dto.RoleDTO;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.model.Role;
/**
 * Classe utilitaire pour convertir entre Utilisateur (entité) et UtilisateurDTO.
 * Ce mapper évite volontairement d'exposer ou de modifier le mot de passe.
 */
public class UtilisateurMapper {

    /**
     * Constructeur privé pour empêcher l’instanciation de cette classe utilitaire.
     */
    private UtilisateurMapper() {
        throw new UnsupportedOperationException("Classe utilitaire - instanciation interdite");
    }

    /**
     * Convertit un Utilisateur en UtilisateurDTO.
     * ATTENTION : Le mot de passe n'est pas exposé ici pour des raisons de sécurité.
     *
     * @param u l'entité Utilisateur
     * @return l'objet UtilisateurDTO correspondant
     */
    public static UtilisateurDTO toDTO(Utilisateur u) {
        if (u == null) return null;

        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setIdUtilisateur(u.getIdUtilisateur());
        dto.setCleUtilisateur(u.getCleUtilisateur());
        dto.setNom(u.getNom());
        dto.setPrenom(u.getPrenom());
        dto.setEmail(u.getEmail());
        dto.setAdresse(u.getAdresse());
        dto.setTelephone(u.getTelephone());
        dto.setDateInscription(u.getDateInscription());
        // ⚠️ Ne pas inclure le mot de passe dans le DTO (même encodé)

        // Rôle associé
        if (u.getRole() != null) {
            RoleDTO roleDto = new RoleDTO();
            roleDto.setIdRole(u.getRole().getIdRole());
            roleDto.setNomRole(u.getRole().getNomRole());
            roleDto.setDescriptionRole(u.getRole().getDescriptionRole()); // 🛠️ Ajout de la description
            dto.setRole(roleDto);
        }

        return dto;
    }

    /**
     * Convertit un UtilisateurDTO en entité Utilisateur.
     * Le mot de passe est géré à part, typiquement dans le contrôleur ou service.
     *
     * @param dto le DTO reçu
     * @return l'entité Utilisateur à sauvegarder
     */
    public static Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null) return null;

        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(dto.getIdUtilisateur());
        u.setCleUtilisateur(dto.getCleUtilisateur());
        u.setNom(dto.getNom());
        u.setPrenom(dto.getPrenom());
        u.setEmail(dto.getEmail());
        u.setAdresse(dto.getAdresse());
        u.setTelephone(dto.getTelephone());
        u.setDateInscription(dto.getDateInscription());
        u.setMotDePasse(dto.getMotDePasse()); // 🛡️ ici, il est déjà encodé dans le contrôleur

        // Rôle reconstruit depuis DTO
        if (dto.getRole() != null) {
            Role r = new Role();
            r.setIdRole(dto.getRole().getIdRole());
            u.setRole(r);
        }

        return u;
    }
}