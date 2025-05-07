package com.ticketjo.ticketjo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) pour l'entité Role. Utilisé pour représenter le
 * rôle attribué à un utilisateur.
 */
public class RoleDTO {

	private Long idRole;

	@NotBlank(message = "Le nom du rôle est obligatoire")
	@Size(min = 4, max = 20, message = "Le nom du rôle doit contenir entre 4 et 20 caractères")
	private String nomRole;
	
	private String descriptionRole;

	// Getters et Setters

	public Long getIdRole() {
		return idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}

	public String getNomRole() {
		return nomRole;
	}

	public void setNomRole(String nomRole) {
		this.nomRole = nomRole;
	}

	public String getDescriptionRole() {
		return descriptionRole;
	}

	public void setDescriptionRole(String descriptionRole) {
		this.descriptionRole = descriptionRole;
	}
}