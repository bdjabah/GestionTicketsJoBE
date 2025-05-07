package com.ticketjo.ticketjo_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.repository.PaiementRepository;
import com.ticketjo.ticketjo_backend.service.PaiementService;

@Service
public class PaiementServiceImpl implements PaiementService {

	private final PaiementRepository paiementRepository;
	private final CommandeRepository commandeRepository;

	public PaiementServiceImpl(PaiementRepository paiementRepository, CommandeRepository commandeRepository) {
		this.paiementRepository = paiementRepository;
		this.commandeRepository = commandeRepository;
	}

	@Override
	public Paiement creerPaiement(Paiement paiement) {
		return paiementRepository.save(paiement);
	}

	@Override
	public Paiement trouverParCommande(Long idCommande) {
		Commande commande = commandeRepository.findById(idCommande).orElse(null);
		if (commande != null) {
			return paiementRepository.findByCommande(commande);
		}
		return null;
	}

	@Override
	public List<Paiement> listerPaiementsParStatut(StatutPaiement statut) {
		return paiementRepository.findByStatut(statut);
	}

	@Override
	public List<Paiement> listerPaiementsUtilisateur(Long idUtilisateur) {
		return paiementRepository.findByCommande_Utilisateur_IdUtilisateur(idUtilisateur);
	}
}