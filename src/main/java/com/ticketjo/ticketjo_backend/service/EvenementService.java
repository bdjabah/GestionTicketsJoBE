package com.ticketjo.ticketjo_backend.service;

import java.time.LocalDate;
import java.util.List;

import com.ticketjo.ticketjo_backend.model.Evenement;

public interface EvenementService {

	Evenement creerEvenement(Evenement evenement); // Créer un événement

	List<Evenement> rechercherParNom(String nomEvenement); // Rechercher par nom

	List<Evenement> rechercherParDiscipline(String discipline); // Rechercher par discipline

	List<Evenement> rechercherParDate(LocalDate dateEvenement); // Rechercher par date

	List<Evenement> rechercherParLieu(String lieuEvenement); // Rechercher par lieu
}