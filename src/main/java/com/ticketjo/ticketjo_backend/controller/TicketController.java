package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.TicketDTO;
import com.ticketjo.ticketjo_backend.mapper.TicketMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.service.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	/**
	 * Endpoint pour créer un nouveau ticket.
	 * 
	 * @param ticket Détails du ticket à créer.
	 * @return Le ticket créé avec un statut 201.
	 */
	@PostMapping
	public ResponseEntity<TicketDTO> createTicket(@RequestBody @Valid TicketDTO ticketDTO) {
		Ticket ticket = TicketMapper.toEntity(ticketDTO);
		Ticket created = ticketService.creerTicket(ticket);
		return new ResponseEntity<>(TicketMapper.toDTO(created), HttpStatus.CREATED);
	}

	/**
	 * Récupérer les tickets associés à une commande.
	 * 
	 * @param idCommande ID de la commande.
	 * @return La liste des tickets sous forme de DTOs.
	 */
	@GetMapping("/commande/{idCommande}")
	public ResponseEntity<List<TicketDTO>> getTicketsByCommande(@PathVariable Long idCommande) {
		Commande commande = new Commande(); // Pour éviter l'appel à un service non inclus ici
		commande.setIdCommande(idCommande);
		List<TicketDTO> dtos = ticketService.obtenirTicketsParCommande(commande).stream().map(TicketMapper::toDTO)
				.toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	/**
	 * Récupérer les tickets associés à un utilisateur.
	 * 
	 * @param idUtilisateur ID de l'utilisateur.
	 * @return Liste des tickets de l'utilisateur en DTO.
	 */
	@GetMapping("/utilisateur/{idUtilisateur}")
	public ResponseEntity<List<TicketDTO>> getTicketsByUtilisateur(@PathVariable Long idUtilisateur) {
		List<TicketDTO> dtos = ticketService.obtenirTicketsParUtilisateur(idUtilisateur).stream()
				.map(TicketMapper::toDTO).toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	/**
	 * Récupérer les tickets d'un événement.
	 * 
	 * @param idEvenement ID de l'événement.
	 * @return Liste des tickets lié à l'événement.
	 */
	@GetMapping("/evenement/{idEvenement}")
	public ResponseEntity<List<TicketDTO>> getTicketsByEvenement(@PathVariable Long idEvenement) {
		Evenement evenement = new Evenement(); // Même logique simplifiée
		evenement.setIdEvenement(idEvenement);
		List<TicketDTO> dtos = ticketService.obtenirTicketsParEvenement(evenement).stream().map(TicketMapper::toDTO)
				.toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	/**
	 * Récupérer un ticket par sa clé unique.
	 * 
	 * @param cleTicket Clé unique du ticket.
	 * @return Ticket correspondant ou 404 si absent.
	 */
	@GetMapping("/cle/{cleTicket}")
	public ResponseEntity<TicketDTO> getTicketByCle(@PathVariable String cleTicket) {
		Ticket ticket = ticketService.trouverTicketParCle(cleTicket);
		if (ticket != null) {
			return new ResponseEntity<>(TicketMapper.toDTO(ticket), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}