package com.ticketjo.ticketjo_backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Data Transfer Object (DTO) pour l'entité Paiement. Utilisé pour transférer
 * les données de paiement sans exposer l'entité complète.
 */
public class PaiementDTO {

	private Long idPaiement;

	@NotBlank(message = "Le statut du paiement est obligatoire")
	private String statut; // on expose le statut sous forme de String

	@NotNull(message = "Le montant du paiement est obligatoire")
	@PositiveOrZero(message = "Le montant ne peut pas être négatif")
	private Double montant; // correspond à montantPaiement

	@NotNull(message = "La date du paiement est obligatoire")
	private LocalDate datePaiement;

	@NotBlank(message = "La méthode de paiement est obligatoire")
	private String methodePaiement;

	@NotNull(message = "L'identifiant de la commande est obligatoire")
	private Long idCommande; // pour lier au DTO de Commande si besoin

	// Getters & Setters

	public Long getIdPaiement() {
		return idPaiement;
	}

	public void setIdPaiement(Long idPaiement) {
		this.idPaiement = idPaiement;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public LocalDate getDatePaiement() {
		return datePaiement;
	}

	public void setDatePaiement(LocalDate datePaiement) {
		this.datePaiement = datePaiement;
	}

	public String getMethodePaiement() {
		return methodePaiement;
	}

	public void setMethodePaiement(String methodePaiement) {
		this.methodePaiement = methodePaiement;
	}

	public Long getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(Long idCommande) {
		this.idCommande = idCommande;
	}
}