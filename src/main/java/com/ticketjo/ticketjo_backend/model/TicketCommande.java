package com.ticketjo.ticketjo_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicketCommande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commandeid", nullable = false)
    private Commande commande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idticket", nullable = false)
    private TicketCatalogue ticketCatalogue;

    @Column(nullable = false)
    private Integer quantite;
    
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;
}