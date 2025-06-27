package com.ticketjo.ticketjo_backend.service;

import com.ticketjo.ticketjo_backend.model.TicketCommande;

import java.util.List;

/**
 * Interface de service pour la gestion des TicketCommande.
 * Définit les opérations métier disponibles pour manipuler les données.
 */
public interface TicketCommandeService {

	 /**
     * Enregistre une nouvelle ligne de commande (TicketCommande).
     * Cette méthode prend une entité complète et la persiste dans la base de données.
     *
     * @param ticketCommande L'entité représentant la ligne de commande à sauvegarder.
     * @return L'entité TicketCommande enregistrée avec son identifiant généré.
     */
    TicketCommande save(TicketCommande ticketCommande);

    /**
     * Récupère toutes les lignes de commande pour une commande donnée.
     * 
     * @param commandeId l'identifiant de la commande
     * @return liste des TicketCommande associés
     */
    List<TicketCommande> findByCommande(Long commandeId);
}