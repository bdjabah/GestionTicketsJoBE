package com.ticketjo.ticketjo_backend.dto;

/**
 * Data Transfer Object (DTO) pour l'entité Ticket.
 * Utilisé pour transférer les données de ticket sans exposer l'entité complète.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TicketCatalogueDTO {

	private Long idTicket;
	
	@NotNull(message = "Le prix du ticket est obligatoire")
	private Double prixTicket;
	
	@NotNull(message = "Le stock est obligatoire")
	private Integer stock;

	@NotBlank(message = "Le type de ticket est obligatoire")
	private String typeTicket;
	
	@NotNull(message = "La capacité est obligatoire")
	private Integer capacite; 

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


	public String getTypeTicket() {
		return typeTicket;
	}

	public void setTypeTicket(String typeTicket) {
		this.typeTicket = typeTicket;
	}

	public Integer getCapacite() {
		return capacite;
	}

	public void setCapacite(Integer capacite) {
		this.capacite = capacite;
	}

	public String getImageTicket() {
		return imageTicket;
	}

	public void setImageTicket(String imageTicket) {
		this.imageTicket = imageTicket;
	}
	
	// Calcul dynamique du statut dans le DTO
		public String getStatutTicket() {
			return stock != null && stock > 0 ? "DISPONIBLE" : "EPUISE";
		}
}