package com.ticketjo.ticketjo_backend.model;

import java.time.LocalDate;
import java.util.List;

import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class Commande {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCommande;
	
	private LocalDate dateCommande;

	@Enumerated(EnumType.STRING) // ← syntaxe correcte
	private StatutCommande statutCommande;

	private double totalCommande;

	@ManyToOne
	@JoinColumn(name = "utilisateurId")
	private Utilisateur utilisateur;

	@OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
	private List<TicketVendu> ticketsVendus;

	@OneToOne(mappedBy = "commande", cascade = CascadeType.ALL)
	private Paiement paiement;
	
	@OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
	private List<TicketCommande> ticketsCommandes;

	// Getters & Setters
}