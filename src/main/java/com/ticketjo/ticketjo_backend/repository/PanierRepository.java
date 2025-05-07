package com.ticketjo.ticketjo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ticketjo.ticketjo_backend.model.Panier;
import java.util.List;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {

	List<Panier> findByUtilisateur_IdUtilisateur(Long idUtilisateur); // Tous les articles dans le panier d'un utilisateur

	void deleteByUtilisateur_IdUtilisateur(Long idUtilisateur); // Vider le panier d’un utilisateur

	boolean existsByUtilisateur_IdUtilisateur(Long idUtilisateur); // Savoir si un panier existe pour un utilisateur

}