package com.ticketjo.ticketjo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.ticketjo.ticketjo_backend.model.TicketVendu;
import com.ticketjo.ticketjo_backend.model.enums.StatutTicket;

public interface TicketVenduRepository extends JpaRepository<TicketVendu, Long> {

    // Trouver par utilisateur
    List<TicketVendu> findByUtilisateur_IdUtilisateur(Long idUtilisateur);

    // Trouver par commande
    List<TicketVendu> findByCommande_IdCommande(Long idCommande);

    // Trouver par clé unique
    TicketVendu findByCleTicket(String cleTicket);

    // Trouver par statut
    List<TicketVendu> findByStatutTicket(StatutTicket statutTicket);
    
}