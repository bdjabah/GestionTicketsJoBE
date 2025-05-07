package com.ticketjo.ticketjo_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.service.CommandeService;

@Service
public class CommandeServiceImpl implements CommandeService {

	private final CommandeRepository commandeRepository;

	public CommandeServiceImpl(CommandeRepository commandeRepository) {
		this.commandeRepository = commandeRepository;
	}

	@Override
	public Commande creerCommande(Commande commande) {
		return commandeRepository.save(commande);
	}

	@Override
	public List<Commande> listerCommandesUtilisateur(Long idUtilisateur) {
		return commandeRepository.findByUtilisateur_IdUtilisateur(idUtilisateur);
	}

	@Override
	public Commande obtenirDerniereCommandeUtilisateur(Long idUtilisateur) {
		return commandeRepository.findTopByUtilisateur_IdUtilisateurOrderByDateCommandeDesc(idUtilisateur).orElse(null);
	}

	@Override
	public List<Commande> listerCommandesParStatut(String statutCommande) {
		StatutCommande statut = StatutCommande.valueOf(statutCommande.toUpperCase()); // convertit la String en Enum
		return commandeRepository.findByStatutCommande(statut);
	}
}