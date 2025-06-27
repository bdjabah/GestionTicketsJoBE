package com.ticketjo.ticketjo_backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Data Transfer Object (DTO) pour l'entité Commande. Utilisé pour transférer
 * les données de commande sans exposer l'entité complète.
 */

public class CommandeDTO {

	private Long idCommande;
	private List<TicketVenduDTO> ticketsVendus;
	
	@PastOrPresent(message = "La date de commande ne peut pas être dans le futur")
	private LocalDate dateCommande;
	
	private Long idUtilisateur;


	@NotNull(message = "Le statut de la commande est obligatoire")
    //@Size(min = 3, max = 50, message = "Le statut doit contenir entre 3 et 50 caractères")
	private StatutCommande statut;
	
	@PositiveOrZero(message = "Le total de la commande ne peut pas être négatif")
	private Double totalCommande;
	
	private List<TicketCommandeDTO> tickets;	
	
	// Getters & Setters

	public Long getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(Long idCommande) {
		this.idCommande = idCommande;
	}

	public LocalDate getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(LocalDate dateCommande) {
		this.dateCommande = dateCommande;
	}
	public void setStatut(StatutCommande statut) {
		this.statut = statut;
	}
	public StatutCommande getStatut() {
		return statut;
	}
	public Double getTotalCommande() {
		return totalCommande;
	}

	public void setTotalCommande(Double totalCommande) {
		this.totalCommande = totalCommande;
	}


	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	
	public List<TicketVenduDTO> getTicketsVendus() {
	    return ticketsVendus;
	}

	public void setTicketsVendus(List<TicketVenduDTO> ticketsVendus) {
	    this.ticketsVendus = ticketsVendus;
	}

	public List<TicketCommandeDTO> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketCommandeDTO> tickets) {
		this.tickets = tickets;
	}
}