package com.ticketjo.ticketjo_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour transporter les informations de login.
 */
public class LoginDTO {

    @NotBlank(message = "L'e-mail est requis")
    @Email(message = "Format d'e-mail invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    private String motDePasse;

    // Getters & Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}