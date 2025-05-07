package com.ticketjo.ticketjo_backend.model;

import java.time.LocalDate;

import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiement;
    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;
    private double montantPaiement;
    private LocalDate datePaiement;
    private String methodePaiement;
  
    @OneToOne
    @JoinColumn(name = "commandeId")
    private Commande commande;

    // Getters & Setters
  
}