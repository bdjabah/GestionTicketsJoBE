package com.ticketjo.ticketjo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) pour l'entité TicketCommande.
 * Utilisé pour transférer les données sans exposer les entités complètes.
 */
public class TicketCommandeDTO {

    private Long idTicketCommande;

    @NotNull(message = "L'identifiant de la commande est obligatoire")
    private Long commandeId;

    @NotNull(message = "L'identifiant du ticket catalogue est obligatoire")
    private Long ticketCatalogueId;

    @NotNull(message = "La quantité est obligatoire")
    private Integer quantite;
    
    @NotBlank(message = "Le nom du participant est requis")
    private String nom;

    @NotBlank(message = "Le prénom du participant est requis")
    private String prenom;

    // Getters & Setters

    public Long getIdTicketCommande() {
        return idTicketCommande;
    }

    public void setIdTicketCommande(Long idTicketCommande) {
        this.idTicketCommande = idTicketCommande;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    public Long getTicketCatalogueId() {
        return ticketCatalogueId;
    }

    public void setTicketCatalogueId(Long ticketCatalogueId) {
        this.ticketCatalogueId = ticketCatalogueId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
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
}