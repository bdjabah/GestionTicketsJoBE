package com.ticketjo.ticketjo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketjo.ticketjo_backend.model.TicketCommande;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommandeRepository extends JpaRepository<TicketCommande, Long> {

    // Récupérer tous les tickets commandés pour une commande donnée
    List<TicketCommande> findByCommande_IdCommande(Long commandeId);

    // Optionnel : récupérer par ticket catalogue
    List<TicketCommande> findByTicketCatalogue_IdTicket(Long ticketCatalogueId);
}
