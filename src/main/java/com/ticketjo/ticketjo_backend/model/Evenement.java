package com.ticketjo.ticketjo_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@Entity
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvenement;

    private String nomEvenement;
    private String discipline;
    private LocalDate dateEvenement;
    private String lieuEvenement;
    private String descriptionEvenement;
    private String imageUrl; // ✅ nouveau champ pour l'image

    // Constructeur sans la liste de tickets
    public Evenement(Long idEvenement, String nomEvenement, String discipline, LocalDate dateEvenement,
                     String lieuEvenement, String descriptionEvenement, String imageUrl) {
        this.idEvenement = idEvenement;
        this.nomEvenement = nomEvenement;
        this.discipline = discipline;
        this.dateEvenement = dateEvenement;
        this.lieuEvenement = lieuEvenement;
        this.descriptionEvenement = descriptionEvenement;
        this.imageUrl = imageUrl;
        // tickets reste null
    }
}