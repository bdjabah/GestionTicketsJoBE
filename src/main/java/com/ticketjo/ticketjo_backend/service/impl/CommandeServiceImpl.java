package com.ticketjo.ticketjo_backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.dto.TicketCommandeDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.model.TicketCommande;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.repository.PaiementRepository;
import com.ticketjo.ticketjo_backend.repository.TicketCatalogueRepository;
import com.ticketjo.ticketjo_backend.repository.TicketCommandeRepository;
import com.ticketjo.ticketjo_backend.service.CommandeService;
import com.ticketjo.ticketjo_backend.repository.UtilisateurRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommandeServiceImpl implements CommandeService {

	private final CommandeRepository commandeRepository;
	@Autowired
	private TicketCommandeRepository ticketCommandeRepository;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private TicketCatalogueRepository ticketCatalogueRepository;

	@Autowired
	private PaiementRepository paiementRepository; // Injection du PaiementRepository

	// Constructeur modifié pour injecter PaiementRepository
	public CommandeServiceImpl(CommandeRepository commandeRepository, PaiementRepository paiementRepository) {
		this.commandeRepository = commandeRepository;
		this.paiementRepository = paiementRepository; // Initialisation du PaiementRepository
	}

	@Override

	public Commande creerCommande(CommandeDTO commandeDTO) {
		Commande commande = new Commande();
		commande.setDateCommande(commandeDTO.getDateCommande());
		commande.setStatutCommande(StatutCommande.EN_ATTENTE);
		commande.setTotalCommande(commandeDTO.getTotalCommande());
		

		// Associe l'utilisateur si nécessaire
		if (commandeDTO.getIdUtilisateur() != null) {
			Utilisateur utilisateur = utilisateurRepository.findById(commandeDTO.getIdUtilisateur())
					.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
			commande.setUtilisateur(utilisateur);
		}

		// 1. On enregistre la commande d'abord
		Commande savedCommande = commandeRepository.save(commande);

		// 2. Ensuite, on enregistre les TicketCommande associés
		for (TicketCommandeDTO ticketDTO : commandeDTO.getTickets()) {
			TicketCatalogue catalogue = ticketCatalogueRepository.findById(ticketDTO.getTicketCatalogueId())
					.orElseThrow(() -> new RuntimeException("TicketCatalogue introuvable"));

			TicketCommande ticketCommande = new TicketCommande();
			ticketCommande.setCommande(savedCommande);
			ticketCommande.setTicketCatalogue(catalogue);
			ticketCommande.setQuantite(ticketDTO.getQuantite());
			ticketCommande.setNom(ticketDTO.getNom());
			ticketCommande.setPrenom(ticketDTO.getPrenom());

			ticketCommandeRepository.save(ticketCommande);
		}

		return savedCommande;
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
		StatutCommande statut = StatutCommande.valueOf(statutCommande.toUpperCase());
		return commandeRepository.findByStatutCommande(statut);
	}

	@Override
	public Commande changerStatut(Long idCommande, StatutCommande nouveauStatut) {
		Commande commande = commandeRepository.findById(idCommande)
				.orElseThrow(() -> new EntityNotFoundException("Commande non trouvée : " + idCommande));

		StatutCommande ancienStatut = commande.getStatutCommande();

		if (!transitionValide(ancienStatut, nouveauStatut)) {
			throw new IllegalStateException("Transition de statut invalide : " + ancienStatut + " → " + nouveauStatut);
		}

		commande.setStatutCommande(nouveauStatut);
		return commandeRepository.save(commande);
	}
	@Override
	public Commande recupererCommandeParId(Long idCommande) {
	    return commandeRepository.findById(idCommande)
	            .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée avec l'ID : " + idCommande));
	}
	@Override
	public Paiement mettreAJourPaiement(Paiement paiement) {
		// On vérifie si le paiement existe déjà
		Paiement existingPaiement = paiementRepository.findById(paiement.getIdPaiement())
				.orElseThrow(() -> new RuntimeException("Paiement introuvable"));

		// Mise à jour des informations du paiement
		existingPaiement.setMontantPaiement(paiement.getMontantPaiement());
		existingPaiement.setStatut(paiement.getStatut()); // Par exemple, mettre à jour le statut
		existingPaiement.setDatePaiement(paiement.getDatePaiement());
		existingPaiement.setMethodePaiement(paiement.getMethodePaiement()); // Mettre à jour la méthode de paiement

		// Sauvegarde le paiement mis à jour
		return paiementRepository.save(existingPaiement); // Retourne le paiement mis à jour
	}

	// Logique métier de transitions autorisées
	private boolean transitionValide(StatutCommande actuel, StatutCommande prochain) {
		return switch (actuel) {
		case EN_ATTENTE -> prochain == StatutCommande.PAYEE || prochain == StatutCommande.ANNULEE;
		case PAYEE -> prochain == StatutCommande.PRETE || prochain == StatutCommande.ANNULEE;
		case PRETE -> prochain == StatutCommande.VALIDE || prochain == StatutCommande.ANNULEE;
		case VALIDE -> prochain == StatutCommande.TERMINEE;
		case TERMINEE, ANNULEE -> false;
		};
	}
}