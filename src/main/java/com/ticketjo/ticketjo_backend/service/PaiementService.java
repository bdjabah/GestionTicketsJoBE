package com.ticketjo.ticketjo_backend.service;

import java.util.List;

import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;

public interface PaiementService {

    Paiement creerPaiement(Paiement paiement); // Enregistrer un paiement

    Paiement trouverParCommande(Long idCommande); // Trouver le paiement associé à une commande

    List<Paiement> listerPaiementsParStatut(StatutPaiement statut); // Lister paiements par statut

    List<Paiement> listerPaiementsUtilisateur(Long idUtilisateur); // Lister tous les paiements d'un utilisateur
    
    // Ajouté pour le webhook Stripe
    void marquerPaiementValide(String paymentIntentId);

    void marquerPaiementEchoue(String paymentIntentId);
    
    Paiement trouverParIntentId(String intentId);
    
    
}
