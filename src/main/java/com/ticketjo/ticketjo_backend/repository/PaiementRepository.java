package com.ticketjo.ticketjo_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;


	@Repository
	public interface PaiementRepository extends JpaRepository<Paiement, Long> {

	    // Pour retrouver un paiement par commande
	    Paiement findByCommande(Commande commande);

	    // Tous les paiements d’un statut donné (ex: en attente)
	    List<Paiement> findByStatut(StatutPaiement statut);

	    // Tous les paiements d’un utilisateur via l’ID de commande
	    List<Paiement> findByCommande_Utilisateur_IdUtilisateur(Long idUtilisateur);

	}