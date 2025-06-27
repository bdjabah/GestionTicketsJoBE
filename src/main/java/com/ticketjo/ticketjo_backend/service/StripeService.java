package com.ticketjo.ticketjo_backend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.ticketjo.ticketjo_backend.config.StripeProperties;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class StripeService {

    private final StripeProperties stripeProperties;
    private final PaiementService paiementService;
    private final CommandeService commandeService;
    private static final Logger logger = Logger.getLogger(StripeService.class.getName());

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getApiKey();
        logger.info("Clé API Stripe initialisée");
    }

    public PaymentIntent createPaymentIntent(Double amount, Long commandeId) throws StripeException {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }

        long amountInCents = (long) (amount * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(stripeProperties.getCurrency())
                .addPaymentMethodType("card")
                .putMetadata("commandeId", String.valueOf(commandeId))
                .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);

            // ✅ Lien explicite avec la commande
            Commande commande = commandeService.recupererCommandeParId(commandeId);
            if (commande == null) {
                throw new RuntimeException("La commande associée au paiement est introuvable");
            }

            Paiement paiement = new Paiement();
            paiement.setMontantPaiement(amount);
            paiement.setDatePaiement(LocalDate.now());
            paiement.setStatut(StatutPaiement.EN_ATTENTE);
            paiement.setMethodePaiement("stripe");
            paiement.setPaymentIntentId(intent.getId());
            paiement.setCommande(commande); // ✅ Lien clé

            Paiement existingPaiement = paiementService.trouverParIntentId(intent.getId());

            if (existingPaiement == null) {
                paiementService.creerPaiement(paiement);
                logger.info("Nouveau paiement créé pour le PaymentIntent : " + intent.getId());
            } else {
                existingPaiement.setMontantPaiement(amount);
                paiementService.mettreAJourPaiement(existingPaiement);
                logger.info("Paiement existant mis à jour pour le PaymentIntent : " + intent.getId());
            }

            logger.info("PaymentIntent créé avec succès : " + intent.getId());
            return intent;

        } catch (StripeException e) {
            logger.severe("Erreur Stripe lors de la création du PaymentIntent : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Erreur interne lors de la création du PaymentIntent : " + e.getMessage());
            throw new RuntimeException("Erreur interne : " + e.getMessage(), e);
        }
    }
}