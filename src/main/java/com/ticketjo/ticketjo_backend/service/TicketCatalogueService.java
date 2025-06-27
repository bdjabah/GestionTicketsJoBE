package com.ticketjo.ticketjo_backend.service;

import java.util.List;
import java.util.Optional;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;

public interface TicketCatalogueService {

	  TicketCatalogue creerTicket(TicketCatalogue ticketCatalogue); // Créer un nouveau ticket

	    TicketCatalogue mettreAJourTicket(TicketCatalogue ticketCatalogue); // Mettre à jour un ticket existant

	    void supprimerTicket(Long idTicket); // Supprimer un ticket par son ID

	    List<TicketCatalogue> obtenirTousLesTickets(); // Récupérer tous les tickets

	    Optional<TicketCatalogue> getTicketById(Long id); // Récupérer un ticket par ID

	    List<TicketCatalogue> obtenirTicketsDisponibles(); // Récupérer tous les tickets avec stock > 0

	    List<TicketCatalogue> rechercherParTypeTicket(String type); // Rechercher des tickets par type
	    
}

