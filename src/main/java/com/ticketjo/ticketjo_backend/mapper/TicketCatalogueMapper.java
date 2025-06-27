package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.TicketCatalogueDTO;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;

public class TicketCatalogueMapper {

    private TicketCatalogueMapper() {
        throw new UnsupportedOperationException("Classe utilitaire");
    }

    public static TicketCatalogueDTO toDTO(TicketCatalogue ticketCatalogue) {
        if (ticketCatalogue == null) return null;

        TicketCatalogueDTO dto = new TicketCatalogueDTO();
        dto.setIdTicket(ticketCatalogue.getIdTicket());
        dto.setTypeTicket(ticketCatalogue.getTypeTicket());
        dto.setPrixTicket(ticketCatalogue.getPrixTicket());
        dto.setStock(ticketCatalogue.getStock());
        dto.setCapacite(ticketCatalogue.getCapacite());

        // Image : préfixe si non vide
        String image = ticketCatalogue.getImageTicket();
        dto.setImageTicket((image != null && !image.isBlank()) ? "/uploads/" + image : null);

        return dto;
    }

    public static TicketCatalogue toEntity(TicketCatalogueDTO dto) {
        if (dto == null) return null;

        TicketCatalogue ticketCatalogue = new TicketCatalogue();
        ticketCatalogue.setIdTicket(dto.getIdTicket());
        ticketCatalogue.setTypeTicket(dto.getTypeTicket());
        ticketCatalogue.setPrixTicket(dto.getPrixTicket());
        ticketCatalogue.setStock(dto.getStock());
        ticketCatalogue.setCapacite(dto.getCapacite());

        // Enlever le préfixe "/uploads/" si présent
        String image = dto.getImageTicket();
        if (image != null && image.startsWith("/uploads/")) {
            ticketCatalogue.setImageTicket(image.substring("/uploads/".length()));
        } else {
            ticketCatalogue.setImageTicket(image);
        }

        return ticketCatalogue;
    }
}