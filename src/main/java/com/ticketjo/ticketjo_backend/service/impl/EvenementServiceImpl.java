package com.ticketjo.ticketjo_backend.service.impl;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.repository.EvenementRepository;
import com.ticketjo.ticketjo_backend.service.EvenementService;

@Service
public class EvenementServiceImpl implements EvenementService {

    private final EvenementRepository evenementRepository;

    public EvenementServiceImpl(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    /**
     * Enregistre un nouvel événement.
     */
    @Override
    public Evenement creerEvenement(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    /**
     * Met à jour un événement existant.
     */
    @Override
    public Evenement updateEvenement(Evenement evenement) {
        Optional<Evenement> existing = evenementRepository.findById(evenement.getIdEvenement());
        if (existing.isPresent()) {
            return evenementRepository.save(evenement);
        } else {
            throw new IllegalArgumentException("Événement introuvable avec l'ID " + evenement.getIdEvenement());
        }
    }

    /**
     * Supprime un événement par son ID.
     */
    @Override
    public void supprimerEvenement(Long id) {
        evenementRepository.deleteById(id);
    }

    /**
     * Récupère tous les événements.
     */
    @Override
    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    @Override
    public List<Evenement> rechercherParNom(String nomEvenement) {
        return evenementRepository.findByNomEvenementContainingIgnoreCase(nomEvenement);
    }

    @Override
    public List<Evenement> rechercherParDiscipline(String discipline) {
        return evenementRepository.findByDisciplineContainingIgnoreCase(discipline);
    }

    @Override
    public List<Evenement> rechercherParDate(LocalDate dateEvenement) {
        return evenementRepository.findByDateEvenement(dateEvenement);
    }

    @Override
    public List<Evenement> rechercherParLieu(String lieuEvenement) {
        return evenementRepository.findByLieuEvenementContainingIgnoreCase(lieuEvenement);
    }
}