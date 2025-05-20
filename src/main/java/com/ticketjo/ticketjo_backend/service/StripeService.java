package com.ticketjo.ticketjo_backend.service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.ticketjo.ticketjo_backend.config.StripeProperties;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service métier pour gérer les paiements Stripe.
 * Ce service s'occupe de :
 * - Initialiser la configuration Stripe
 * - Créer un PaymentIntent via l'API Stripe
 * - Enregistrer le paiement dans la base avec statut "EN_ATTENTE"
 */
@Service
@RequiredArgsConstructor
public class StripeService {

    private final StripeProperties stripeProperties;
    private final PaiementService paiementService;

    /**
     * Initialise la clé API Stripe au lancement de l'application.
     * Récupérée depuis StripeProperties injecté via application.properties.
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getApiKey();
    }

    /**
     * Crée un PaymentIntent Stripe et enregistre une ligne de paiement dans la base.
     *
     * @param amount Le montant total à facturer (en euros, ex : 24.99)
     * @return Le PaymentIntent Stripe contenant le clientSecret
     * @throws StripeException En cas de problème de communication avec Stripe
     */
    public PaymentIntent createPaymentIntent(Double amount) throws StripeException {
        // Sécurité : on s'assure que le montant est bien positif
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }

        // Stripe exige un montant en centimes, donc on convertit en long
        long amountInCents = (long) (amount * 100);

        // On construit les paramètres nécessaires pour créer le PaymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amountInCents)
            .setCurrency(stripeProperties.getCurrency()) // Exemple : "eur"
            .addPaymentMethodType("card")
            .build();

        // On envoie la requête à Stripe
        PaymentIntent intent = PaymentIntent.create(params);

        // Préparation de l'objet Paiement à enregistrer en base
        Paiement paiement = new Paiement();
        paiement.setMontantPaiement(amount);
        paiement.setDatePaiement(LocalDate.now());
        paiement.setStatut(StatutPaiement.EN_ATTENTE); // En attente car pas encore validé
        paiement.setMethodePaiement("stripe");
        paiement.setPaymentIntentId(intent.getId());

        // 🔒 Anti-doublon : n'enregistre le paiement que s'il n'existe pas déjà
        if (paiementService.trouverParIntentId(intent.getId()) == null) {
            paiementService.creerPaiement(paiement);
        }

        // On renvoie l'objet PaymentIntent pour que le frontend puisse obtenir le clientSecret
        return intent;
    }
}