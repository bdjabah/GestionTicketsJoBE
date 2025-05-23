package com.ticketjo.ticketjo_backend.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.ticketjo.ticketjo_backend.config.StripeProperties;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.service.CommandeService;
import com.ticketjo.ticketjo_backend.service.PaiementService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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
    private final CommandeService commandeService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        System.out.println("Début du traitement du webhook Stripe.");

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
            System.out.println("Signature Stripe validée.");
        } catch (Exception e) {
            System.out.println("Signature Stripe invalide : " + e.getMessage());
            return ResponseEntity.badRequest().body("Signature invalide");
        }

        System.out.println("Événement Stripe reçu : " + event.getType());

        var deserializer = event.getDataObjectDeserializer();

        try {
            Object obj = deserializer.getObject().orElseGet(() -> {
                try {
                    System.out.println("Objet non désérialisé automatiquement. JSON brut : " +
                            event.getData().getObject().toString());
                    return deserializer.deserializeUnsafe();
                } catch (Exception e) {
                    System.out.println("Erreur de désérialisation forcée : " + e.getMessage());
                    return null;
                }
            });

            if (obj instanceof PaymentIntent intent) {
                String intentId = intent.getId();
                System.out.println("PaymentIntent ID : " + intentId);

                switch (event.getType()) {
                    case "payment_intent.succeeded" -> {
                        System.out.println("Type : payment_intent.succeeded");
                        paiementService.marquerPaiementValide(intentId);
                        System.out.println("Statut du paiement mis à jour à VALIDE");

                        var paiement = paiementService.trouverParIntentId(intentId);
                        if (paiement != null) {
                            System.out.println("Paiement trouvé pour PaymentIntent ID : " + intentId);
                            var utilisateur = (paiement.getCommande() != null) ? paiement.getCommande().getUtilisateur() : null;

                            if (utilisateur != null) {
                                System.out.println("Utilisateur trouvé : " + utilisateur.getIdUtilisateur());

                                var commande = new Commande();
                                commande.setDateCommande(LocalDate.now());
                                commande.setStatutCommande(StatutCommande.EN_COURS);
                                commande.setTotalCommande(paiement.getMontantPaiement());
                                commande.setUtilisateur(utilisateur);
                                commande.setPaiement(paiement);

                                paiement.setCommande(commande);

                                commandeService.creerCommande(commande);
                                paiementService.creerPaiement(paiement);
                               
                                System.out.println("Commande créée et sauvegardée.");
                            } else {
                                System.out.println("Utilisateur introuvable pour ce paiement.");
                            }
                        } else {
                            System.out.println("Paiement introuvable pour intent ID : " + intentId);
                        }
                    }
                    case "payment_intent.payment_failed" -> {
                        System.out.println("Type : payment_intent.payment_failed");
                        paiementService.marquerPaiementEchoue(intentId);
                        System.out.println("Statut du paiement mis à jour à ECHOUE");
                    }
                    default -> System.out.println("Événement non géré : " + event.getType());
                }

                return ResponseEntity.ok("Webhook Stripe traité");
            } else {
                System.out.println("Objet désérialisé non reconnu ou null.");
                return ResponseEntity.badRequest().body("Objet inattendu ou null");
            }

        } catch (Exception e) {
            System.out.println("Exception attrapée pendant le traitement : " + e.getMessage());
            return ResponseEntity.badRequest().body("Erreur de traitement");
        }
    }
}
///**
// * Contrôleur REST pour écouter les webhooks Stripe.
// * Stripe envoie automatiquement des notifications à cette route
// * lorsque l'état d'un paiement change (succès, échec...).
// */
//
//@RestController
//@RequestMapping("/api/stripe")
//@RequiredArgsConstructor
//public class StripeWebhookController {
//
//    private final PaiementService paiementService;
//    private final StripeProperties stripeProperties;
//    private final CommandeService commandeService;
//    @PostMapping("/webhook")
//    public ResponseEntity<String> handleStripeWebhook(
//            @RequestBody String payload,
//            @RequestHeader("Stripe-Signature") String sigHeader) {
//
//        Event event;
//        try {
//            event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
//        } catch (Exception e) {
//            System.out.println("Signature Stripe invalide : " + e.getMessage());
//            return ResponseEntity.badRequest().body("Signature invalide");
//        }
//
//        System.out.println("Événement Stripe reçu : " + event.getType());
//
//        // Désérialiseur de l'objet contenu dans data.object
//        var deserializer = event.getDataObjectDeserializer();
//
//        try {
//            Object obj = deserializer.getObject().orElseGet(() -> {
//                try {
//                    // 🔍 LOG DU JSON BRUT SI DÉSÉRIALISATION ÉCHOUE
//                    System.out.println("Objet non désérialisé automatiquement. JSON brut reçu : " +
//                            event.getData().getObject().toString());
//
//                    // Tentative de désérialisation forcée
//                    return deserializer.deserializeUnsafe();
//                } catch (Exception e) {
//                    System.out.println("Erreur lors de la désérialisation forcée : " + e.getMessage());
//                    return null;
//                }
//            });
//
//            if (obj instanceof PaymentIntent intent) {
//                String intentId = intent.getId();
//                System.out.println("✅ PaymentIntent ID : " + intentId);
//
//                switch (event.getType()) {
//                    case "payment_intent.succeeded" -> {
//                        paiementService.marquerPaiementValide(intentId);
//                        System.out.println("Paiement validé !");
//
//                        //  Création automatique de commande après paiement validé
//                        var paiement = paiementService.trouverParIntentId(intentId);
//                        if (paiement != null && paiement.getCommande() != null) {
//                            var utilisateur = paiement.getCommande().getUtilisateur();
//
//                            var commande = new Commande();
//                            commande.setDateCommande(LocalDate.now());
//                            commande.setStatutCommande(StatutCommande.EN_COURS);
//                            commande.setTotalCommande(paiement.getMontantPaiement());
//                            commande.setUtilisateur(utilisateur);
//                            commande.setPaiement(paiement);
//
//                            paiement.setCommande(commande);
//
//                            // Appel direct au service commande
//                            commandeService.creerCommande(commande);
//                            paiementService.creerPaiement(paiement);
//
//                            System.out.println("Commande créée automatiquement pour l'utilisateur ID: " + utilisateur.getIdUtilisateur());
//                        } else {
//                            System.out.println("Impossible de créer la commande : paiement ou utilisateur manquant.");
//                        }
//                    }
//                    case "payment_intent.payment_failed" -> {
//                        paiementService.marquerPaiementEchoue(intentId);
//                        System.out.println("Paiement échoué !");
//                    }
//                    default -> System.out.println("Événement ignoré : " + event.getType());
//                }
//
//                return ResponseEntity.ok("Webhook Stripe traité");
//            } else {
//                System.out.println("Objet inattendu ou null : " + (obj != null ? obj.getClass().getName() : "null"));
//                return ResponseEntity.badRequest().body("Objet inattendu ou null");
//            }
//
//        } catch (Exception e) {
//            System.out.println("Exception globale lors du traitement : " + e.getMessage());
//            return ResponseEntity.badRequest().body("Erreur de traitement");
//        }
//    }
//}