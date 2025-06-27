package com.ticketjo.ticketjo_backend.service;

import java.util.List;
import java.util.Optional;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.TicketVendu;
import com.ticketjo.ticketjo_backend.model.enums.StatutTicket;

public interface TicketVenduService {

    TicketVendu creerTicketVendu(TicketVendu ticketVendu); // Enregistrer un ticket vendu

    List<TicketVendu> obtenirTousLesTicketsVendus(); // Récupérer tous les tickets vendus

    Optional<TicketVendu> getTicketVenduParId(Long id); // Récupérer un ticket vendu par ID

    TicketVendu trouverParCleTicket(String cle); // Chercher par clé unique

    List<TicketVendu> obtenirParIdUtilisateur(Long idUtilisateur); // Tous les tickets d’un utilisateur

    List<TicketVendu> obtenirParIdCommande(Long idCommande); // Tous les tickets liés à une commande

    List<TicketVendu> obtenirParStatut(StatutTicket statutTicket); // Par statut (UTILISÉ, NON_UTILISÉ)

    void supprimerTicketVendu(Long id); // Supprimer par ID
    
    /**
     * Génère les tickets vendus pour une commande donnée après paiement validé.
     * @param commande La commande validée.
     */
    void genererTicketsPourCommande(Commande commande);
}