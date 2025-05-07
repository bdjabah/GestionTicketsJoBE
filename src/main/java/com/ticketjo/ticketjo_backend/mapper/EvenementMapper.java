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
    public static EvenementDTO toDTO(Evenement e) {
        EvenementDTO dto = new EvenementDTO();
        dto.setIdEvenement(e.getIdEvenement());
        dto.setNomEvenement(e.getNomEvenement());
        dto.setDiscipline(e.getDiscipline());
        dto.setLieuEvenement(e.getLieuEvenement());
        dto.setDateEvenement(e.getDateEvenement());
        dto.setDescriptionEvenement(e.getDescriptionEvenement());
        return dto;
    }

    public static Evenement toEntity(EvenementDTO dto) {
        Evenement e = new Evenement();
        e.setIdEvenement(dto.getIdEvenement());
        e.setNomEvenement(dto.getNomEvenement());
        e.setDiscipline(dto.getDiscipline());
        e.setLieuEvenement(dto.getLieuEvenement());
        e.setDateEvenement(dto.getDateEvenement());
        e.setDescriptionEvenement(dto.getDescriptionEvenement());
        return e;
    }
}