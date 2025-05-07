package com.ticketjo.ticketjo_backend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.ticketjo.ticketjo_backend.config.StripeProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service permettant de communiquer avec l'API Stripe pour créer des paiements.
 */
@Service
@RequiredArgsConstructor
public class StripeService {

    private final StripeProperties stripeProperties;

    /**
     * Initialise la clé API Stripe après injection de la config.
     */

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getKey();
    }

    /**
     * Crée un PaymentIntent Stripe pour un montant donné.
     * Stripe attend les montants en centimes.
     *
     * @param amount Le montant total en euros.
     * @return Un PaymentIntent Stripe contenant le clientSecret.
     * @throws StripeException En cas d’erreur de communication avec Stripe.
     */
    public PaymentIntent createPaymentIntent(Double amount) throws StripeException {
        long amountInCents = (long) (amount * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amountInCents)
            .setCurrency("eur")
            .addPaymentMethodType("card")
            .build();

        return PaymentIntent.create(params);
    }
}