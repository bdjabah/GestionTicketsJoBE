package com.ticketjo.ticketjo_backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ticketjo.ticketjo_backend.model.Panier;
import com.ticketjo.ticketjo_backend.repository.PanierRepository;
import com.ticketjo.ticketjo_backend.service.PanierService;

@Service
public class PanierServiceImpl implements PanierService {

	private final PanierRepository panierRepository;

	public PanierServiceImpl(PanierRepository panierRepository) {
		this.panierRepository = panierRepository;
	}

	@Override
	public Panier ajouterPanier(Panier panier) {
		return panierRepository.save(panier);
	}

	@Override
	public void supprimerPanier(Long idPanier) {
		panierRepository.deleteById(idPanier);
	}

	@Override
	public List<Panier> listerPaniersParUtilisateur(Long idUtilisateur) {
		return panierRepository.findByUtilisateur_IdUtilisateur(idUtilisateur);
	}
}
