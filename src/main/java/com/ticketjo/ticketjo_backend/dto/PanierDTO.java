package com.ticketjo.ticketjo_backend.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * Data Transfer Object (DTO) pour l'entité Panier.
 * Utilisé pour transférer les données de panier sans exposer l'entité complète.
 */
public class PanierDTO {

    private Long idPanier;
    
    @PastOrPresent(message = "La date de création ne peut pas être dans le futur")
    private LocalDate dateCreation;
    
    @NotBlank(message = "Le statut du panier est obligatoire")
    private String statutPanier;
    
    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long idUtilisateur;
    private List<CommandeDTO> commandes;

    // Getters & Setters

    public Long getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(Long idPanier) {
        this.idPanier = idPanier;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getStatutPanier() {
        return statutPanier;
    }

    public void setStatutPanier(String statutPanier) {
        this.statutPanier = statutPanier;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public List<CommandeDTO> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<CommandeDTO> commandes) {
        this.commandes = commandes;
    }
}