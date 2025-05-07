package com.ticketjo.ticketjo_backend.service;

import java.util.List;
import com.ticketjo.ticketjo_backend.model.Commande;

public interface CommandeService {

	Commande creerCommande(Commande commande); // Créer une commande

	List<Commande> listerCommandesUtilisateur(Long idUtilisateur); // Voir toutes les commandes d'un utilisateur

	Commande obtenirDerniereCommandeUtilisateur(Long idUtilisateur); // Voir la dernière commande faite par un
																		// utilisateur

	List<Commande> listerCommandesParStatut(String statutCommande); // Voir les commandes par leur statut
}