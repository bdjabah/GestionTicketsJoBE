package com.ticketjo.ticketjo_backend.mapper;


import com.ticketjo.ticketjo_backend.dto.PaiementDTO;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
/**
 * Classe utilitaire pour convertir entre Paiement et PaiementDTO.
 */
public class PaiementMapper {

    public static PaiementDTO toDTO(Paiement paiement) {
        if (paiement == null) return null;

        PaiementDTO dto = new PaiementDTO();
        dto.setIdPaiement(paiement.getIdPaiement());
        dto.setStatut(paiement.getStatut().name());
        dto.setMontant(paiement.getMontantPaiement());
        dto.setDatePaiement(paiement.getDatePaiement());
        dto.setMethodePaiement(paiement.getMethodePaiement());
        dto.setIdCommande(
            paiement.getCommande() != null
                ? paiement.getCommande().getIdCommande()
                : null
        );
        return dto;
    }

    public static Paiement toEntity(PaiementDTO dto) {
        if (dto == null) return null;

        Paiement paiement = new Paiement();
        paiement.setIdPaiement(dto.getIdPaiement());
        paiement.setStatut(StatutPaiement.valueOf(dto.getStatut()));
        paiement.setMontantPaiement(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setMethodePaiement(dto.getMethodePaiement());

        if (dto.getIdCommande() != null) {
            Commande commande = new Commande();
            commande.setIdCommande(dto.getIdCommande());
            paiement.setCommande(commande);
        }
        return paiement;
    }
}