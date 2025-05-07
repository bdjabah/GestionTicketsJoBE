package com.ticketjo.ticketjo_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    private String nomRole;
    private String descriptionRole;

    @OneToMany(mappedBy = "role")
    private List<Utilisateur> utilisateurs;

    // Getters & Setters
}