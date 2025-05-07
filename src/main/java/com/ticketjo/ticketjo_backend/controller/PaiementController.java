package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.PaiementDTO;
import com.ticketjo.ticketjo_backend.mapper.PaiementMapper;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.service.PaiementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des paiements.
 */
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

	private final PaiementService paiementService;

	/**
	 * Crée un nouveau paiement.
	 * 
	 * @param paiement Détails du paiement à enregistrer.
	 * @return Le paiement créé avec un statut HTTP 201 (Created).
	 */
	@PostMapping
	public ResponseEntity<PaiementDTO> creerPaiement(@RequestBody @Valid PaiementDTO paiementDTO) {
		Paiement paiement = PaiementMapper.toEntity(paiementDTO);
		Paiement createdPaiement = paiementService.creerPaiement(paiement);
		return new ResponseEntity<>(PaiementMapper.toDTO(createdPaiement), HttpStatus.CREATED);
	}

	/**
	 * Récupère un paiement par l'identifiant d'une commande.
	 * 
	 * @param idCommande ID de la commande associée au paiement.
	 * @return Le paiement trouvé ou 404 si absent.
	 */
	@GetMapping("/commande/{idCommande}")
	public ResponseEntity<PaiementDTO> trouverParCommande(@PathVariable Long idCommande) {
		Paiement paiement = paiementService.trouverParCommande(idCommande);
		if (paiement == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK);
	}

	/**
	 * Récupère la liste des paiements en fonction de leur statut.
	 * 
	 * @param statut Le statut des paiements (ex: EN_ATTENTE, VALIDE, REFUSE).
	 * @return Liste des paiements correspondant au statut.
	 */
	@GetMapping("/statut/{statut}")
	public ResponseEntity<List<PaiementDTO>> listerPaiementsParStatut(@PathVariable StatutPaiement statut) {
		List<PaiementDTO> paiements = paiementService.listerPaiementsParStatut(statut).stream()
				.map(PaiementMapper::toDTO).toList();
		return new ResponseEntity<>(paiements, HttpStatus.OK);
	}

	/**
	 * Récupère tous les paiements d'un utilisateur donné.
	 * 
	 * @param idUtilisateur ID de l'utilisateur.
	 * @return Liste des paiements liés à cet utilisateur.
	 */
	@GetMapping("/utilisateur/{idUtilisateur}")
	public ResponseEntity<List<PaiementDTO>> listerPaiementsUtilisateur(@PathVariable Long idUtilisateur) {
		List<PaiementDTO> paiements = paiementService.listerPaiementsUtilisateur(idUtilisateur).stream()
				.map(PaiementMapper::toDTO).toList();
		return new ResponseEntity<>(paiements, HttpStatus.OK);
	}
}