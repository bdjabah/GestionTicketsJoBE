package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.EvenementDTO;
import com.ticketjo.ticketjo_backend.mapper.EvenementMapper;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.service.EvenementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des événements.
 */
@RestController
@RequestMapping("/api/evenements")
@RequiredArgsConstructor
public class EvenementController {

	private final EvenementService evenementService;

	/**
	 * Crée un nouvel événement.
	 * 
	 * @param evenement L'événement à créer.
	 * @return L'événement créé sous forme de DTO ou statut HTTP 201 (Created).
	 */
	@PostMapping
	public ResponseEntity<EvenementDTO> creerEvenement(@RequestBody @Valid EvenementDTO evenementDTO) {
		Evenement evenement = EvenementMapper.toEntity(evenementDTO);
		Evenement created = evenementService.creerEvenement(evenement);
		return new ResponseEntity<>(EvenementMapper.toDTO(created), HttpStatus.CREATED);
	}

	/**
	 * Recherche les événements par nom.
	 * 
	 * @param nomEvenement Le nom à rechercher (partiel ou complet).
	 * @return Liste des événements DTO correspondant au nom.
	 */
	@GetMapping("/nom/{nomEvenement}")
	public ResponseEntity<List<EvenementDTO>> rechercherParNom(@PathVariable String nomEvenement) {
		List<EvenementDTO> dtos = evenementService.rechercherParNom(nomEvenement).stream().map(EvenementMapper::toDTO)
				.toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	/**
	 * Recherche les événements par discipline.
	 * 
	 * @param discipline La discipline à rechercher.
	 * @return Liste des événements correspondant à la discipline.
	 */
	@GetMapping("/discipline/{discipline}")
	public ResponseEntity<List<EvenementDTO>> rechercherParDiscipline(@PathVariable String discipline) {
		List<EvenementDTO> dtos = evenementService.rechercherParDiscipline(discipline).stream()
				.map(EvenementMapper::toDTO).toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	/**
	 * Recherche les événements par date.
	 * 
	 * @param dateEvenement La date de l'événement au format YYYY-MM-DD.
	 * @return Liste des événements ayant lieu à cette date.
	 */
	@GetMapping("/date/{dateEvenement}")
	public ResponseEntity<List<EvenementDTO>> rechercherParDate(@PathVariable String dateEvenement) {
		LocalDate date = LocalDate.parse(dateEvenement);
		List<EvenementDTO> dtos = evenementService.rechercherParDate(date).stream().map(EvenementMapper::toDTO)
				.toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	/**
	 * Recherche les événements par lieu.
	 * 
	 * @param lieuEvenement Le lieu à rechercher.
	 * @return Liste des événements correspondant au lieu.
	 */
	@GetMapping("/lieu/{lieuEvenement}")
	public ResponseEntity<List<EvenementDTO>> rechercherParLieu(@PathVariable String lieuEvenement) {
		List<EvenementDTO> dtos = evenementService.rechercherParLieu(lieuEvenement).stream().map(EvenementMapper::toDTO)
				.toList();
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}