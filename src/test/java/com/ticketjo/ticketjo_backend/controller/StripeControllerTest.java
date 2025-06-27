package com.ticketjo.ticketjo_backend.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.ticketjo.ticketjo_backend.service.StripeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du StripeController
 * - simulateur de paiement réussi
 * - erreur Stripe (serveur)
 * - erreur utilisateur (mauvaise donnée)
 */
class StripeControllerTest {

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private StripeController stripeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialise les mocks
    }

    @Test
    void createPaymentIntent_shouldReturnClientSecret_onSuccess() throws StripeException {
        // GIVEN
        double montant = 42.50;
        long commandeId = 123L;

        PaymentIntent mockIntent = mock(PaymentIntent.class);  // Mock de PaymentIntent
        when(mockIntent.getClientSecret()).thenReturn("secret_123");
        when(mockIntent.getId()).thenReturn("123");  // Mock de l'ID PaymentIntent
        when(stripeService.createPaymentIntent(montant, commandeId)).thenReturn(mockIntent);

        Map<String, Object> request = new HashMap<>();
        request.put("amount", montant);
        request.put("commandeId", commandeId);

        // WHEN
        ResponseEntity<Map<String, String>> response = stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("secret_123", response.getBody().get("clientSecret"));
        assertEquals("123", response.getBody().get("paymentIntentId"));  // Assurez-vous que l'ID est bien "123"
        verify(stripeService).createPaymentIntent(montant, commandeId);  // Vérification que la méthode a bien été appelée
    }
    
    @Test
    void createPaymentIntent_shouldReturn500_onStripeException() throws StripeException {
        // GIVEN
        StripeException stripeException = mock(StripeException.class);  // Utilisez mock() au lieu de new
        when(stripeService.createPaymentIntent(anyDouble(), anyLong())).thenThrow(stripeException);
        
        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10.0);
        request.put("commandeId", 77L);

        // WHEN
        ResponseEntity<Map<String, String>> response = stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").startsWith("Erreur Stripe"));
    }
    
    @Test
    void createPaymentIntent_shouldReturn400_onInvalidAmount() {
        // GIVEN
        Map<String, Object> request = new HashMap<>();
        request.put("amount", "pas un nombre");  // Données incorrectes
        request.put("commandeId", 55L);

        // WHEN
        ResponseEntity<Map<String, String>> response = stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").startsWith("Données invalides"));
    }

    @Test
    void createPaymentIntent_shouldReturn400_onMissingCommandeId() {
        // GIVEN
        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10.0);
        // commandeId manquant

        // WHEN
        ResponseEntity<Map<String, String>> response = stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").startsWith("Les données 'amount' et 'commandeId' sont obligatoires"));
    }
}