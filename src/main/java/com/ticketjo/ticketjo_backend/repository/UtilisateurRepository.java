package com.ticketjo.ticketjo_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
@Repository

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
	Optional<Utilisateur> findByEmail(String email); // pour login
	List<Utilisateur> findByRole(Role role);         // pour filtrer les rôles
	boolean existsByEmail(String email);             // pour éviter les doublons à l'inscription
}

