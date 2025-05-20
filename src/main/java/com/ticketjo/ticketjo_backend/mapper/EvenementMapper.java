package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.EvenementDTO;
import com.ticketjo.ticketjo_backend.model.Evenement;

/**
 * Classe utilitaire pour convertir entre Evenement et EvenementDTO.
 */
public class EvenementMapper {

    // Empêche l’instanciation de cette classe utilitaire
    private EvenementMapper() {
        throw new UnsupportedOperationException("Classe utilitaire");
    }

    /**
     * Convertit une entité Evenement en DTO.
     */
    public static EvenementDTO toDTO(Evenement e) {
        EvenementDTO dto = new EvenementDTO();
        dto.setIdEvenement(e.getIdEvenement());
        dto.setNomEvenement(e.getNomEvenement());
        dto.setDiscipline(e.getDiscipline());
        dto.setLieuEvenement(e.getLieuEvenement());
        dto.setDateEvenement(e.getDateEvenement());
        dto.setDescriptionEvenement(e.getDescriptionEvenement());
        dto.setImageUrl(e.getImageUrl()); // ✅ nouveau champ mappé
        return dto;
    }

    /**
     * Convertit un DTO en entité Evenement.
     */
    public static Evenement toEntity(EvenementDTO dto) {
        Evenement e = new Evenement();
        e.setIdEvenement(dto.getIdEvenement());
        e.setNomEvenement(dto.getNomEvenement());
        e.setDiscipline(dto.getDiscipline());
        e.setLieuEvenement(dto.getLieuEvenement());
        e.setDateEvenement(dto.getDateEvenement());
        e.setDescriptionEvenement(dto.getDescriptionEvenement());
        e.setImageUrl(dto.getImageUrl()); // ✅ nouveau champ mappé
        return e;
    }
}