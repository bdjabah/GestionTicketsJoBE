package com.ticketjo.ticketjo_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
//import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.ticketjo.ticketjo_backend.config.StripeProperties;

import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.service.CommandeService;
import com.ticketjo.ticketjo_backend.service.PaiementService;
import com.ticketjo.ticketjo_backend.service.TicketVenduService;

import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Contrôleur REST pour écouter les webhooks Stripe.
 * Stripe envoie automatiquement des notifications à cette route
 * lorsque l'état d'un paiement change.
 */
@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaiementService paiementService;
    private final StripeProperties stripeProperties;
    private final CommandeService commandeService;
    private final TicketVenduService ticketVenduService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        System.out.println("📩 Réception d'un webhook Stripe");

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
            System.out.println("✅ Signature Stripe validée");
        } catch (Exception e) {
            System.out.println("❌ Signature Stripe invalide : " + e.getMessage());
            return ResponseEntity.badRequest().body("Signature invalide");
        }

        System.out.println("ℹ️ Type d'événement : " + event.getType());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);

            // Récupération du PaymentIntent dans data.object
            JsonNode paymentIntentNode = rootNode.get("data").get("object");

            if (paymentIntentNode == null || paymentIntentNode.get("id") == null) {
                System.out.println("❌ PaymentIntent introuvable ou malformé");
                return ResponseEntity.badRequest().body("PaymentIntent introuvable");
            }

            String intentId = paymentIntentNode.get("id").asText();
            System.out.println("💳 PaymentIntent ID : " + intentId);

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    paiementService.marquerPaiementValide(intentId);
                    System.out.println("🎉 Paiement marqué comme VALIDE");

                    var paiement = paiementService.trouverParIntentId(intentId);
                    if (paiement != null && paiement.getCommande() != null) {
                        var commande = paiement.getCommande();

                        commandeService.changerStatut(commande.getIdCommande(), StatutCommande.PAYEE);
                        System.out.println("📝 Statut de la commande mis à jour à PAYEE");

                        ticketVenduService.genererTicketsPourCommande(commande);
                        System.out.println("🎟️ Tickets générés pour la commande");

                        return ResponseEntity.ok("✅ Paiement confirmé, commande et tickets mis à jour");
                    } else {
                        System.out.println("⚠️ Commande introuvable pour ce paiement");
                        return ResponseEntity.badRequest().body("Commande introuvable pour ce paiement");
                    }
                }

                case "payment_intent.payment_failed" -> {
                    paiementService.marquerPaiementEchoue(intentId);
                    System.out.println("💥 Paiement échoué mis à jour");
                    return ResponseEntity.ok("Paiement échoué");
                }

                default -> {
                    System.out.println("ℹ️ Événement non pris en charge : " + event.getType());
                    return ResponseEntity.ok("Événement ignoré");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du traitement du webhook : " + e.getMessage());
            return ResponseEntity.badRequest().body("Erreur de traitement");
        }
    }
}
