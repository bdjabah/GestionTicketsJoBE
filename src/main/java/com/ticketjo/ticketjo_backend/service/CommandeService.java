package com.ticketjo.ticketjo_backend.service;

import java.util.List;

import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;

public interface CommandeService {

	Commande creerCommande(CommandeDTO commandeDTO); // Créer une commande

	List<Commande> listerCommandesUtilisateur(Long idUtilisateur); // Voir toutes les commandes d'un utilisateur

	Commande obtenirDerniereCommandeUtilisateur(Long idUtilisateur); // Voir la dernière commande faite par unutilisateur

	List<Commande> listerCommandesParStatut(String statutCommande); // Voir les commandes par leur statut
	
	Commande changerStatut(Long idCommande, StatutCommande nouveauStatut);
	 
	Paiement mettreAJourPaiement(Paiement paiement); 
	
	Commande recupererCommandeParId(Long idCommande);//Pour récupérer une commande existante (utile dans StripeService)
	
}