package com.ticketjo.ticketjo_backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPanier;

    private LocalDate dateCreation;
    private String statutPanier;

    @OneToOne
    @JoinColumn(name = "idUtilisateur")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "panier")
    private List<Commande> commandes;

    // Getters & Setters
}