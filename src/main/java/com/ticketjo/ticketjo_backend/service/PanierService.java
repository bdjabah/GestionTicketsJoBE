package com.ticketjo.ticketjo_backend.service;

import java.util.List;
import com.ticketjo.ticketjo_backend.model.Panier;

public interface PanierService {
	
	Panier ajouterPanier(Panier panier); // Ajouter un nouveau panier

	void supprimerPanier(Long idPanier); // Supprimer un panier par son ID

	List<Panier> listerPaniersParUtilisateur(Long idUtilisateur); // Lister les paniers liés à un utilisateur
}