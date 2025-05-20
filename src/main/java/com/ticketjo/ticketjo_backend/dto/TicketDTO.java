package com.ticketjo.ticketjo_backend.dto;

/**
 * Data Transfer Object (DTO) pour l'entité Ticket.
 * Utilisé pour transférer les données de ticket sans exposer l'entité complète.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TicketDTO {

	private Long idTicket;
	private Double prixTicket;
	private Integer stock;

	@NotBlank(message = "La clé du ticket est obligatoire")
	@Size(min = 10, max = 100)
	private String cleTicket;

	@NotBlank(message = "Le QR code est obligatoire")
	private String qrCode;

	@NotBlank(message = "Le type de ticket est obligatoire")
	private String typeTicket;

	private String statutTicket;

	@NotNull(message = "L'identifiant de la commande est obligatoire")
	private Long idCommande;

	@NotNull(message = "L'identifiant de l'événement est obligatoire")
	private Long idEvenement;

	@Size(max = 255)
	@Pattern(regexp = "^(https?://.*|/.*)?$")
	private String imageTicket;

	// Getters & Setters

	public Long getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(Long idTicket) {
		this.idTicket = idTicket;
	}

	public Double getPrixTicket() {
		return prixTicket;
	}

	public void setPrixTicket(Double prixTicket) {
		this.prixTicket = prixTicket;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getCleTicket() {
		return cleTicket;
	}

	public void setCleTicket(String cleTicket) {
		this.cleTicket = cleTicket;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getTypeTicket() {
		return typeTicket;
	}

	public void setTypeTicket(String typeTicket) {
		this.typeTicket = typeTicket;
	}

	public String getStatutTicket() {
		return statutTicket;
	}

	public void setStatutTicket(String statutTicket) {
		this.statutTicket = statutTicket;
	}

	public Long getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(Long idCommande) {
		this.idCommande = idCommande;
	}

	public Long getIdEvenement() {
		return idEvenement;
	}

	public void setIdEvenement(Long idEvenement) {
		this.idEvenement = idEvenement;
	}

	public String getImageTicket() {
		return imageTicket;
	}

	public void setImageTicket(String imageTicket) {
		this.imageTicket = imageTicket;
	}
}