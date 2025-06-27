package com.ticketjo.ticketjo_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

	// Pour voir toutes les commandes d’un utilisateur
    List<Commande> findByUtilisateur_IdUtilisateur(Long idUtilisateur);

    // Pour voir les commandes selon leur statut (EN_ATTENTE, VALIDEE, etc.)
    List<Commande> findByStatutCommande(StatutCommande statutCommande);

    // Pour récupérer la dernière commande passée par un utilisateur
    Optional<Commande> findTopByUtilisateur_IdUtilisateurOrderByDateCommandeDesc(Long idUtilisateur);

    // Pour vérifie si un utilisateur a au moins une commande
    boolean existsByUtilisateur_IdUtilisateur(Long idUtilisateur);

    // Pour récupérer toutes les commandes passées entre deux dates
    List<Commande> findAllByDateCommandeBetween(LocalDate debut, LocalDate fin);
    
 // Nouvelle méthode pour récupérer la commande avec les ticketsCommandes
    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.ticketsCommandes WHERE c.idCommande = :id")
    Optional<Commande> findByIdWithTickets(@Param("id") Long id);
}