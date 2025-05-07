package com.ticketjo.ticketjo_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketjo.ticketjo_backend.model.Evenement;


	@Repository
	public interface EvenementRepository extends JpaRepository<Evenement, Long> {

	    // Rechercher un événement par son nom
	    List<Evenement> findByNomEvenementContainingIgnoreCase(String nomEvenement);

	    // Rechercher par discipline
	    List<Evenement> findByDisciplineContainingIgnoreCase(String discipline);

	    // Rechercher les événements à une date donnée
	    List<Evenement> findByDateEvenement(LocalDate dateEvenement);

	    // Rechercher par lieu
	    List<Evenement> findByLieuEvenementContainingIgnoreCase(String lieuEvenement);
	}