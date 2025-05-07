package com.ticketjo.ticketjo_backend.service;

import java.util.List;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;

public interface TicketService {

	Ticket creerTicket(Ticket ticket); // Créer un nouveau ticket

	List<Ticket> obtenirTicketsParCommande(Commande commande); // Tous les tickets d'une commande

	List<Ticket> obtenirTicketsParUtilisateur(Long idUtilisateur); // Tous les tickets d'un utilisateur

	List<Ticket> obtenirTicketsParEvenement(Evenement evenement); // Tous les tickets d'un événement

	Ticket trouverTicketParCle(String cleTicket); // Chercher un ticket avec sa clé unique
}