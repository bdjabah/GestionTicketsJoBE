package com.ticketjo.ticketjo_backend.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.ticketjo.ticketjo_backend.config.StripeProperties;
import com.ticketjo.ticketjo_backend.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour écouter les webhooks Stripe.
 * Stripe envoie automatiquement des notifications à cette route
 * lorsque l'état d'un paiement change (succès, échec...).
 */
@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaiementService paiementService;
    private final StripeProperties stripeProperties;

    /**
     * Endpoint qui écoute les webhooks Stripe.
     * Stripe envoie des requêtes POST contenant des événements signés.
     * Ce contrôleur vérifie la signature et met à jour le statut du paiement.
     *
     * @param payload Contenu brut envoyé par Stripe (JSON)
     * @param sigHeader En-tête contenant la signature Stripe (à valider)
     * @return 200 OK ou 400 Bad Request si erreur
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {

        // 🔐 On récupère le secret du webhook depuis la config
        String webhookSecret = stripeProperties.getWebhookSecret();
        Event event;

        // ✅ Étape 1 : Vérifier l'authenticité de la signature envoyée par Stripe
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            // ❌ Si la signature est invalide, on rejette la requête
            return ResponseEntity.badRequest().body("Signature invalide");
        }

        // ✅ Étape 2 : Identifier le type d’événement envoyé par Stripe
        String type = event.getType();

        // ✅ Étape 3 : Récupérer le PaymentIntent (informations du paiement concerné)
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (intent == null) {
            // ❌ Si Stripe n’a pas envoyé d’objet valide, on retourne une erreur
            return ResponseEntity.badRequest().body("PaymentIntent manquant");
        }

        // ✅ Étape 4 : Gérer chaque type d’événement Stripe attendu
        switch (type) {
            case "payment_intent.succeeded":
                // 🎉 Paiement réussi : on met à jour le statut en base
                paiementService.marquerPaiementValide(intent.getId());
                break;

            case "payment_intent.payment_failed":
                // ❌ Paiement échoué : on met à jour en base aussi
                paiementService.marquerPaiementEchoue(intent.getId());
                break;

            default:
                // ⚠️ Autres événements ignorés (ex: refund, chargeback)
                break;
        }

        // ✅ Réponse à Stripe : on confirme que le webhook a bien été traité
        return ResponseEntity.ok("Webhook traité");
    }
}