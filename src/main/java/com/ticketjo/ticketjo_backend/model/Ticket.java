package com.ticketjo.ticketjo_backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    private String typeTicket;
    private double prixTicket;
    private Integer stock;
    private String cleTicket;
    private String qrCode;
    private String statutTicket;
    private String imageTicket;

    @ManyToOne
    @JoinColumn(name = "commandeId")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "evenementId")
    private Evenement evenement;
 // Getters & Setters
}
    
