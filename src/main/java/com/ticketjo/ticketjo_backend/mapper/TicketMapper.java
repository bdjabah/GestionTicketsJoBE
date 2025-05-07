package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.TicketDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;

/**
 * Classe utilitaire pour convertir entre l'entité Ticket et le DTO TicketDTO.
 * Utilisée pour isoler la logique de transformation des objets dans l'application.
 */
public class TicketMapper {

    /**
     * Constructeur privé pour empêcher toute instanciation de la classe.
     * La classe est uniquement composée de méthodes statiques.
     */
    private TicketMapper() {
        throw new UnsupportedOperationException("Classe utilitaire");
    }

    /**
     * Convertit une entité Ticket vers un DTO TicketDTO.
     *
     * @param ticket L'entité Ticket à convertir.
     * @return Le DTO correspondant ou null si l'entrée est null.
     */
    public static TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setIdTicket(ticket.getIdTicket());
        dto.setTypeTicket(ticket.getTypeTicket());
        dto.setPrixTicket(ticket.getPrixTicket());
        dto.setDateEvenement(ticket.getDateEvenement());
        dto.setCleTicket(ticket.getCleTicket());
        dto.setQrCode(ticket.getQrCode());
        dto.setStatutTicket(ticket.getStatutTicket());

        // Associe l'identifiant de la commande si elle est présente
        dto.setIdCommande(ticket.getCommande() != null
            ? ticket.getCommande().getIdCommande()
            : null);

        // Associe l'identifiant de l'événement si présent
        dto.setIdEvenement(ticket.getEvenement() != null
            ? ticket.getEvenement().getIdEvenement()
            : null);

        return dto;
    }

    /**
     * Convertit un TicketDTO en entité Ticket.
     *
     * @param dto Le DTO à convertir.
     * @return L'entité Ticket correspondante ou null si l'entrée est null.
     */
    public static Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;

        Ticket ticket = new Ticket();
        ticket.setIdTicket(dto.getIdTicket());
        ticket.setTypeTicket(dto.getTypeTicket());
        ticket.setPrixTicket(dto.getPrixTicket());
        ticket.setDateEvenement(dto.getDateEvenement());
        ticket.setCleTicket(dto.getCleTicket());
        ticket.setQrCode(dto.getQrCode());
        ticket.setStatutTicket(dto.getStatutTicket());

        // Si un ID de commande est fourni, associe une instance Commande correspondante
        if (dto.getIdCommande() != null) {
            Commande commande = new Commande();
            commande.setIdCommande(dto.getIdCommande());
            ticket.setCommande(commande);
        }

        // Si un ID d'événement est fourni, associe une instance Evenement correspondante
        if (dto.getIdEvenement() != null) {
            Evenement evenement = new Evenement();
            evenement.setIdEvenement(dto.getIdEvenement());
            ticket.setEvenement(evenement);
        }

        return ticket;
    }
}