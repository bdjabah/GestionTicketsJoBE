package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.mapper.CommandeMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.service.CommandeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des commandes.
 */
@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

	private final CommandeService commandeService;

	/**
	 * Crée une nouvelle commande à partir d'un DTO validé.
	 *
	 * @param commandeDTO Détails de la commande à créer.
	 * @return La commande créée, sous forme de DTO.
	 */
	@PostMapping
	public ResponseEntity<CommandeDTO> creerCommande(@RequestBody @Valid CommandeDTO commandeDTO) {
		Commande commande = CommandeMapper.toEntity(commandeDTO);
		Commande created = commandeService.creerCommande(commande);
		return new ResponseEntity<>(CommandeMapper.toDTO(created), HttpStatus.CREATED);
	}

	/**
	 * Liste toutes les commandes d'un utilisateur.
	 *
	 * @param idUtilisateur L'ID de l'utilisateur concerné.
	 * @return Liste des commandes de l'utilisateur (DTO).
	 */
	@GetMapping("/utilisateur/{idUtilisateur}")
	public ResponseEntity<List<CommandeDTO>> listerCommandesUtilisateur(@PathVariable Long idUtilisateur) {
		List<CommandeDTO> commandes = commandeService.listerCommandesUtilisateur(idUtilisateur).stream()
				.map(CommandeMapper::toDTO).toList();
		return new ResponseEntity<>(commandes, HttpStatus.OK);
	}

	/**
	 * Récupère la dernière commande passée par un utilisateur.
	 *
	 * @param idUtilisateur L'ID de l'utilisateur concerné.
	 * @return Dernière commande trouvée ou 404 si aucune commande.
	 */
	@GetMapping("/utilisateur/{idUtilisateur}/derniere")
	public ResponseEntity<CommandeDTO> obtenirDerniereCommandeUtilisateur(@PathVariable Long idUtilisateur) {
		Commande commande = commandeService.obtenirDerniereCommandeUtilisateur(idUtilisateur);
		if (commande == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(CommandeMapper.toDTO(commande), HttpStatus.OK);
	}

	/**
	 * Liste toutes les commandes ayant un statut donné.
	 *
	 * @param statutCommande Le statut recherché (ex: EN_ATTENTE, VALIDEE, etc.)
	 * @return Liste des commandes correspondant au statut, sous forme de DTO.
	 */
	@GetMapping("/statut/{statutCommande}")
	public ResponseEntity<List<CommandeDTO>> listerCommandesParStatut(@PathVariable String statutCommande) {
		List<CommandeDTO> commandes = commandeService.listerCommandesParStatut(statutCommande).stream()
				.map(CommandeMapper::toDTO).toList();
		return new ResponseEntity<>(commandes, HttpStatus.OK);
	}
}