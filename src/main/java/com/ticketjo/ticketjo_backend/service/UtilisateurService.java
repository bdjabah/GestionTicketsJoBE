package com.ticketjo.ticketjo_backend.service;

import java.util.List;
import java.util.Optional;

import com.ticketjo.ticketjo_backend.model.Utilisateur;

public interface UtilisateurService {
	Utilisateur createUtilisateur(Utilisateur utilisateur);

	void deleteUtilisateur(Long idUtilisateur);

	Optional<Utilisateur> getUtilisateurByEmail(String email);

	List<Utilisateur> getAllUtilisateurs();
}