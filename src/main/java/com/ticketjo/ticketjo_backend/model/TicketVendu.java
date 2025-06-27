package com.ticketjo.ticketjo_backend.model;

import java.time.LocalDateTime;

import com.ticketjo.ticketjo_backend.model.enums.StatutTicket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
@Table(name = "ticket_vendu")
public class TicketVendu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicketVendu;

    @Column(nullable = false, unique = true)
    private String cleTicket;
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;
    @Column(nullable = false)
    private String qrCode;

    @Enumerated(EnumType.STRING)
    private  StatutTicket statutTicket; 

    @Column(nullable = false)
    private LocalDateTime dateAchat;

    @ManyToOne
    @JoinColumn(name = "idcommande")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "idutilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "idticket")
    private TicketCatalogue ticketCatalogue;

    @Column(nullable = false)
    private String typeTicket;

    @Column(nullable = false)
    private Double prixTicket;

    private String imageTicket;
}