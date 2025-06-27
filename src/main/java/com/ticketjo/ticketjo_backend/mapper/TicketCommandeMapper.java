package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.TicketCommandeDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.model.TicketCommande;

import org.springframework.stereotype.Component;

@Component
public class TicketCommandeMapper {

    /**
     * Convertit un DTO vers une entité TicketCommande.
     * ⚠️ Nécessite les entités Commande et TicketCatalogue déjà chargées.
     */
    public TicketCommande toEntity(TicketCommandeDTO dto, Commande commande, TicketCatalogue ticketCatalogue) {
        TicketCommande entity = new TicketCommande();
        entity.setIdTicketCommande(dto.getIdTicketCommande()); // facultatif
        entity.setCommande(commande);
        entity.setTicketCatalogue(ticketCatalogue);
        entity.setQuantite(dto.getQuantite());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        return entity;
    }

    /**
     * Convertit une entité TicketCommande vers son DTO.
     * Ne retourne que les IDs pour ne pas exposer les entités liées.
     */
    public TicketCommandeDTO toDTO(TicketCommande entity) {
        TicketCommandeDTO dto = new TicketCommandeDTO();
        dto.setIdTicketCommande(entity.getIdTicketCommande());
        dto.setCommandeId(entity.getCommande().getIdCommande());
        dto.setTicketCatalogueId(entity.getTicketCatalogue().getIdTicket());
        dto.setQuantite(entity.getQuantite());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        return dto;
    }
}