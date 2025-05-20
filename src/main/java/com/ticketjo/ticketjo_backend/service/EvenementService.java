package com.ticketjo.ticketjo_backend.service;

import java.time.LocalDate;
import java.util.List;

import com.ticketjo.ticketjo_backend.model.Evenement;

public interface EvenementService {

	 Evenement creerEvenement(Evenement evenement);

	    /**
	     * Met à jour un événement existant.
	     */
	    Evenement updateEvenement(Evenement evenement);

	    /**
	     * Supprime un événement par son ID.
	     */
	    void supprimerEvenement(Long id);

	    /**
	     * Récupère tous les événements.
	     */
	    List<Evenement> getAllEvenements();

	    /**
	     * Recherche par nom d'événement.
	     */
	    List<Evenement> rechercherParNom(String nomEvenement);

	    /**
	     * Recherche par discipline.
	     */
	    List<Evenement> rechercherParDiscipline(String discipline);

	    /**
	     * Recherche par date.
	     */
	    List<Evenement> rechercherParDate(LocalDate dateEvenement);

	    /**
	     * Recherche par lieu.
	     */
	    List<Evenement> rechercherParLieu(String lieuEvenement);

}