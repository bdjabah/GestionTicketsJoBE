package com.ticketjo.ticketjo_backend.controller;

import java.util.HashMap;
import java.util.Map;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.ticketjo.ticketjo_backend.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des paiements via Stripe.
 * Ce contrôleur expose un endpoint permettant au frontend de générer un PaymentIntent,
 * et de recevoir un clientSecret pour finaliser le paiement avec Stripe.js côté client.
 */

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor

public class StripeController {
	 private final StripeService stripeService;
	@PostMapping("/create-payment-intent")
	public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> data) {
	    try {
	        Double amount = Double.parseDouble(data.get("amount").toString());
	        Long commandeId = Long.parseLong(data.get("commandeId").toString());

	        PaymentIntent intent = stripeService.createPaymentIntent(amount, commandeId);

	        Map<String, String> response = new HashMap<>();
	        response.put("clientSecret", intent.getClientSecret());

	        return ResponseEntity.ok(response);

	    } catch (StripeException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "Erreur Stripe : " + e.getMessage()));

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(Map.of("error", "Données invalides : " + e.getMessage()));
	    }
	}
	}

//
//   
//
//    @PostMapping("/create-payment-intent")
//    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> data) {
//        try {
//            Double amount = Double.parseDouble(data.get("amount").toString());
//            PaymentIntent intent = stripeService.createPaymentIntent(amount);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("clientSecret", intent.getClientSecret());
//
//            return ResponseEntity.ok(response);
//
//        } catch (StripeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Erreur Stripe : " + e.getMessage()));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Données invalides : " + e.getMessage()));
//        }
//    }
//}

//@RestController
//@RequestMapping("/api/stripe")
//@RequiredArgsConstructor
//public class StripeController {
//
//    // Service métier qui encapsule l'appel à l'API Stripe
//    private final StripeService stripeService;
//
//    /**
//     * Endpoint pour créer un PaymentIntent côté Stripe.
//     * Le montant est reçu dans le corps de la requête.
//     * Stripe renvoie un clientSecret que le frontend doit utiliser
//     * pour confirmer le paiement via Stripe.js.
//     *
//     * Exemple de requête POST (JSON) :
//     * {
//     *   "amount": 24.99
//     * }
//     *
//     * @param data Un JSON contenant le montant à facturer (clé "amount").
//     * @return Le clientSecret Stripe ou une erreur avec statut HTTP.
//     */
//    @PostMapping("/create-payment-intent")
//    public ResponseEntity<Map<String, String>> createPaymentIntent(
//            @RequestBody Map<String, Object> data) {
//        try {
//            Double amount = Double.parseDouble(data.get("amount").toString());
//
//            // Appel au service pour créer le PaymentIntent
//            PaymentIntent intent = stripeService.createPaymentIntent(amount);
//
//            // On renvoie uniquement le clientSecret, le frontend en a besoin pour finaliser
//            Map<String, String> response = new HashMap<>();
//            response.put("clientSecret", intent.getClientSecret());
//
//            return ResponseEntity.ok(response);
//
//        } catch (StripeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Erreur Stripe : " + e.getMessage()));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Données invalides : " + e.getMessage()));
//        }
//    }
//}