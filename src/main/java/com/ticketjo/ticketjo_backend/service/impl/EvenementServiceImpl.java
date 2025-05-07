package com.ticketjo.ticketjo_backend.service.impl;

import java.time.LocalDate;
import java.util.List;

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

	@Override
	public Evenement creerEvenement(Evenement evenement) {
		return evenementRepository.save(evenement);
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
