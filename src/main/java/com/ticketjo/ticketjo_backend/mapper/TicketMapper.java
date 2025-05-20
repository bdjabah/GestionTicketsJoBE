package com.ticketjo.ticketjo_backend.mapper;
import com.ticketjo.ticketjo_backend.dto.TicketDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;

public class TicketMapper {

    private TicketMapper() {
        throw new UnsupportedOperationException("Classe utilitaire");
    }

    public static TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setIdTicket(ticket.getIdTicket());
        dto.setTypeTicket(ticket.getTypeTicket());
        dto.setPrixTicket(ticket.getPrixTicket());
        dto.setStock(ticket.getStock());
        dto.setCleTicket(ticket.getCleTicket());
        dto.setQrCode(ticket.getQrCode());
        dto.setStatutTicket(ticket.getStatutTicket());

        if (ticket.getImageTicket() != null && !ticket.getImageTicket().isEmpty()) {
            dto.setImageTicket("/uploads/" + ticket.getImageTicket());
        } else {
            dto.setImageTicket(null);
        }

        dto.setIdCommande(ticket.getCommande() != null ? ticket.getCommande().getIdCommande() : null);
        dto.setIdEvenement(ticket.getEvenement() != null ? ticket.getEvenement().getIdEvenement() : null);

        return dto;
    }

    public static Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;

        Ticket ticket = new Ticket();
        ticket.setIdTicket(dto.getIdTicket());
        ticket.setTypeTicket(dto.getTypeTicket());
        ticket.setPrixTicket(dto.getPrixTicket());
        ticket.setStock(dto.getStock());
        ticket.setCleTicket(dto.getCleTicket());
        ticket.setQrCode(dto.getQrCode());
        ticket.setStatutTicket(dto.getStatutTicket());

        if (dto.getImageTicket() != null && dto.getImageTicket().startsWith("/uploads/")) {
            ticket.setImageTicket(dto.getImageTicket().substring("/uploads/".length()));
        } else {
            ticket.setImageTicket(dto.getImageTicket());
        }

        if (dto.getIdCommande() != null) {
            Commande commande = new Commande();
            commande.setIdCommande(dto.getIdCommande());
            ticket.setCommande(commande);
        }

        if (dto.getIdEvenement() != null) {
            Evenement evenement = new Evenement();
            evenement.setIdEvenement(dto.getIdEvenement());
            ticket.setEvenement(evenement);
        }

        return ticket;
    }
}