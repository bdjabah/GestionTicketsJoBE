package com.ticketjo.ticketjo_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
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

	@OneToMany(mappedBy = "evenement")
	private List<Ticket> tickets;

	// Getters & Setters
	public Evenement(Long idEvenement, String nomEvenement, String discipline, LocalDate dateEvenement,
			String lieuEvenement, String descriptionEvenement) {
		this.idEvenement = idEvenement;
		this.nomEvenement = nomEvenement;
		this.discipline = discipline;
		this.dateEvenement = dateEvenement;
		this.lieuEvenement = lieuEvenement;
		this.descriptionEvenement = descriptionEvenement;
// tickets reste null par défaut
	}
}
