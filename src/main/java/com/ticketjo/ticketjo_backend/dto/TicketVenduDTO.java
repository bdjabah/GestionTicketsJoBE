package com.ticketjo.ticketjo_backend.dto;

import java.time.LocalDateTime;

import com.ticketjo.ticketjo_backend.model.enums.StatutTicket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class TicketVenduDTO {
	
	@NotBlank(message = "La clé du ticket est obligatoire.")
	private String cleTicket;
	
	@NotBlank(message = "Le nom est requis.")
    private String nom;

	@NotBlank(message = "Le prénom est requis.")
    private String prenom;
    
	@NotBlank(message = "Le QR code est requis.")
	private String qrCode;

	@NotBlank(message = "Le statut du ticket est requis.")
	private StatutTicket statutTicket;

	@NotNull(message = "La date d'achat est obligatoire.")
	private LocalDateTime dateAchat;

	@NotNull(message = "L'identifiant de l'utilisateur est requis.")
	private Long idUtilisateur;

	@NotNull(message = "L'identifiant de la commande est requis.")
	private Long idCommande;

	@NotNull(message = "L'identifiant du ticket source est requis.")
	private Long idTicketCatalogue;

	@NotBlank(message = "Le type de ticket est obligatoire.")
	private String typeTicket;

	@NotNull(message = "Le prix du ticket est obligatoire.")
	@Positive(message = "Le prix doit être supérieur à zéro.")
	private Double prixTicket;

	@Size(max = 255)
	@Pattern(regexp = "^(https?://.*|/.*)?$")
	private String imageTicket;
	
	public String getCleTicket() {
		return cleTicket;
	}

	public void setCleTicket(String cleTicket) {
		this.cleTicket = cleTicket;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public LocalDateTime getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(LocalDateTime dateAchat) {
		this.dateAchat = dateAchat;
	}

	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public Long getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(Long idCommande) {
		this.idCommande = idCommande;
	}

	public Long getIdTicketCatalogue() {
		return idTicketCatalogue;
	}

	public void setIdTicketCatalogue(Long idTicketCatalogue) {
		this.idTicketCatalogue = idTicketCatalogue;
	}

	public String getTypeTicket() {
		return typeTicket;
	}

	public void setTypeTicket(String typeTicket) {
		this.typeTicket = typeTicket;
	}

	public Double getPrixTicket() {
		return prixTicket;
	}

	public void setPrixTicket(Double prixTicket) {
		this.prixTicket = prixTicket;
	}

	public String getImageTicket() {
		return imageTicket;
	}

	public void setImageTicket(String imageTicket) {
		this.imageTicket = imageTicket;
	}

	private Long idTicketVendu;

	public Long getIdTicketVendu() {
		return idTicketVendu;
	}

	public void setIdTicketVendu(Long idTicketVendu) {
		this.idTicketVendu = idTicketVendu;
	}
	
	public StatutTicket getStatutTicket() {
		return statutTicket;
	}

	public void setStatutTicket(StatutTicket statutTicket) {
		this.statutTicket = statutTicket;
	}
}
