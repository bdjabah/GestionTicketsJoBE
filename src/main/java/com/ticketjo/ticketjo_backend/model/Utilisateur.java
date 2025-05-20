package com.ticketjo.ticketjo_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class Utilisateur {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;
	@Column(name = "cleutilisateur", nullable = false, unique = true)
	private String cleUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String adresse;
    private String telephone;
    private LocalDate dateInscription;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @OneToMany(mappedBy = "utilisateur")
    private List<Commande> commandes;

    // Getters & Setters
}

