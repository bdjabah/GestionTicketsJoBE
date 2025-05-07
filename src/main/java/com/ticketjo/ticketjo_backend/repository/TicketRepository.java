package com.ticketjo.ticketjo_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	// Liste des tickets associés à une commande
	List<Ticket> findByCommande(Commande commande);

	// Liste des tickets pour un utilisateur (via commande)
	List<Ticket> findByCommande_Utilisateur_IdUtilisateur(Long idUtilisateur);

	// Tous les tickets d’un événement
	List<Ticket> findByEvenement(Evenement evenement);

	// Recherche par clé de ticket (genre hash unique, pratique pour validation)
	Ticket findByCleTicket(String cleTicket);
}
