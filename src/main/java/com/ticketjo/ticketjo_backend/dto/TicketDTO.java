package com.ticketjo.ticketjo_backend.dto;

/**
 * Data Transfer Object (DTO) pour l'entité Ticket.
 * Utilisé pour transférer les données de ticket sans exposer l'entité complète.
 */

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketDTO {

    private Long idTicket;
    
    private Double prixTicket;
    private LocalDate dateEvenement;
    
    @NotBlank(message = "La clé du ticket est obligatoire")
    @Size(min = 10, max = 100, message = "La clé du ticket doit contenir entre 10 et 100 caractères")
    private String cleTicket;
    
    @NotBlank(message = "Le QR code est obligatoire")
    private String qrCode; // Peut être en base64 ou URL
    
    @NotBlank(message = "Le type de ticket est obligatoire")
    private String typeTicket;
    
    private String statutTicket;
    
    @NotNull(message = "L'identifiant de la commande est obligatoire")
    private Long idCommande;
    
    @NotNull(message = "L'identifiant de l'événement est obligatoire")
    private Long idEvenement;

    // Getters & Setters

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
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

    public LocalDate getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(LocalDate dateEvenement) {
        this.dateEvenement = dateEvenement;
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
}