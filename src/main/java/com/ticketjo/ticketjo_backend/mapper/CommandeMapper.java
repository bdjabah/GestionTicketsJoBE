package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.model.Utilisateur;

import java.util.stream.Collectors;

public class CommandeMapper {

	public static CommandeDTO toDTO(Commande commande) {
	    if (commande == null)
	        return null;

	    CommandeDTO dto = new CommandeDTO();
	    dto.setIdCommande(commande.getIdCommande());
	    dto.setDateCommande(commande.getDateCommande());
	    dto.setStatut(commande.getStatutCommande());
	    dto.setTotalCommande(commande.getTotalCommande());

	    if (commande.getUtilisateur() != null) {
	        dto.setIdUtilisateur(commande.getUtilisateur().getIdUtilisateur());
	    }

	    if (commande.getTickets() != null) {
	        dto.setTickets(commande.getTickets().stream().map(TicketMapper::toDTO).collect(Collectors.toList()));
	    }

	    return dto;
	}

	public static Commande toEntity(CommandeDTO dto) {
	    if (dto == null)
	        return null;

	    Commande commande = new Commande();
	    commande.setIdCommande(dto.getIdCommande());
	    commande.setDateCommande(dto.getDateCommande());
	    commande.setStatutCommande(dto.getStatut());
	    commande.setTotalCommande(dto.getTotalCommande());

	    if (dto.getIdUtilisateur() != null) {
	        Utilisateur utilisateur = new Utilisateur();
	        utilisateur.setIdUtilisateur(dto.getIdUtilisateur());
	        commande.setUtilisateur(utilisateur);
	    }

	    if (dto.getTickets() != null) {
	        var listeTickets = dto.getTickets().stream().map(ticketDto -> {
	            Ticket t = TicketMapper.toEntity(ticketDto);
	            t.setCommande(commande);
	            return t;
	        }).toList();
	        commande.setTickets(listeTickets);
	    }

	    return commande;
	}
}