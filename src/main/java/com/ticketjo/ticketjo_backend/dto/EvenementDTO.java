package com.ticketjo.ticketjo_backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
/**
 * Data Transfer Object (DTO) pour l'entité Evenement. Utilisé pour transférer
 * les données d'événement sans exposer l'entité complète.
 */
public class EvenementDTO {

	private Long idEvenement;

	@NotBlank(message = "Le nom de l'événement est obligatoire")
	@Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
	private String nomEvenement;

	@NotBlank(message = "La discipline est obligatoire")
	private String discipline;

	@FutureOrPresent(message = "La date de l'événement doit être aujourd'hui ou dans le futur")
	private LocalDate dateEvenement;

	@NotBlank(message = "Le lieu de l'événement est obligatoire")
	private String lieuEvenement;

	@Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
	private String descriptionEvenement;

	// Constructeurs
	public EvenementDTO() {
	}

	public EvenementDTO(Long idEvenement, String nomEvenement, String discipline, LocalDate dateEvenement,
			String lieuEvenement, String descriptionEvenement) {
		this.idEvenement = idEvenement;
		this.nomEvenement = nomEvenement;
		this.discipline = discipline;
		this.dateEvenement = dateEvenement;
		this.lieuEvenement = lieuEvenement;
		this.descriptionEvenement = descriptionEvenement;
	}

	// Getters et Setters

	public Long getIdEvenement() {
		return idEvenement;
	}

	public void setIdEvenement(Long idEvenement) {
		this.idEvenement = idEvenement;
	}

	public String getNomEvenement() {
		return nomEvenement;
	}

	public void setNomEvenement(String nomEvenement) {
		this.nomEvenement = nomEvenement;
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	public LocalDate getDateEvenement() {
		return dateEvenement;
	}

	public void setDateEvenement(LocalDate dateEvenement) {
		this.dateEvenement = dateEvenement;
	}

	public String getLieuEvenement() {
		return lieuEvenement;
	}

	public void setLieuEvenement(String lieuEvenement) {
		this.lieuEvenement = lieuEvenement;
	}

	public String getDescriptionEvenement() {
		return descriptionEvenement;
	}

	public void setDescriptionEvenement(String descriptionEvenement) {
		this.descriptionEvenement = descriptionEvenement;
	}
}