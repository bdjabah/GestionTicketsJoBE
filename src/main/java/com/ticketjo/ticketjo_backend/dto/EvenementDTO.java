package com.ticketjo.ticketjo_backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object (DTO) pour l'entité Evenement.
 * Utilisé pour transférer les données d'événement sans exposer l'entité complète.
 */
public class EvenementDTO {

    private Long idEvenement;

    @NotBlank(message = "Le nom de l'événement est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nomEvenement;

    @NotBlank(message = "La discipline est obligatoire")
    @Size(min = 2, max = 50, message = "La discipline doit contenir entre 2 et 50 caractères")
    private String discipline;

    @FutureOrPresent(message = "La date de l'événement doit être aujourd'hui ou dans le futur")
    private LocalDate dateEvenement;

    @NotBlank(message = "Le lieu de l'événement est obligatoire")
    @Size(min = 2, max = 100, message = "Le lieu doit contenir entre 2 et 100 caractères")
    private String lieuEvenement;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String descriptionEvenement;

    @Size(max = 255, message = "L'URL de l'image ne doit pas dépasser 255 caractères")
    @Pattern(regexp = "^(https?://.*|/.*)?$", message = "L'URL de l'image doit être une URL valide ou un chemin relatif")
    private String imageUrl;

    // Constructeurs
    public EvenementDTO() {
    }

    public EvenementDTO(Long idEvenement, String nomEvenement, String discipline, LocalDate dateEvenement,
                        String lieuEvenement, String descriptionEvenement, String imageUrl) {
        this.idEvenement = idEvenement;
        this.nomEvenement = nomEvenement;
        this.discipline = discipline;
        this.dateEvenement = dateEvenement;
        this.lieuEvenement = lieuEvenement;
        this.descriptionEvenement = descriptionEvenement;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}