package com.ticketjo.ticketjo_backend.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
/**
 * Data Transfer Object (DTO) pour l'entité Utilisateur.
 * Ce DTO est utilisé pour transférer les données nécessaires entre les couches
 * de l'application sans exposer l'entité complète.
 */
public class UtilisateurDTO {

    private Long idUtilisateur;

    /**
     * Clé unique générée pour l'utilisateur lors de la création du compte.
     * Utilisée pour des opérations internes et non exposée à l'utilisateur final.
     */
    private String cleUtilisateur;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Le mot de passe doit contenir au moins une majuscule, une minuscule et un chiffre"
    )
    private String motDePasse;
    
    @NotBlank(message = "L'adresse est obligatoire")
    @Size(min = 5, max = 100, message = "L'adresse doit contenir entre 5 et 100 caractères")
    private String adresse;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(
        regexp = "^\\+?[0-9]{7,15}$",
        message = "Le numéro de téléphone doit être valide (ex : +33612345678)"
    )
    private String telephone;

    private LocalDate dateInscription;

    /**
     * Rôle attribué à l'utilisateur.
     * Représenté ici par un autre DTO pour éviter les dépendances circulaires.
     */
    private RoleDTO role;

    // Getters et Setters

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getCleUtilisateur() {
        return cleUtilisateur;
    }

    public void setCleUtilisateur(String cleUtilisateur) {
        this.cleUtilisateur = cleUtilisateur;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getMotDePasse() {
        return motDePasse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
}