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
	    	// Vérification des données
	    	 if (data.get("amount") == null || data.get("commandeId") == null) {
	                return ResponseEntity.badRequest().body(Map.of("error", "Les données 'amount' et 'commandeId' sont obligatoires"));
	            }
	    	 
	        Double amount = Double.parseDouble(data.get("amount").toString());
	        Long commandeId = Long.parseLong(data.get("commandeId").toString());

	        if (amount <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le montant doit être supérieur à zéro"));
            }
	        
	        // Création du PaymentIntent via le service
	        PaymentIntent intent = stripeService.createPaymentIntent(amount, commandeId);

	        // Préparation de la réponse avec clientSecret et paymentIntentId
	        Map<String, String> response = new HashMap<>();
	        response.put("clientSecret", intent.getClientSecret());
	        response.put("paymentIntentId", intent.getId());

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

