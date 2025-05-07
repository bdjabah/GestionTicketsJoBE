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

class StripeControllerTest {

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private StripeController stripeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPaymentIntent_shouldReturnClientSecret_onSuccess() throws Exception {
        // GIVEN
        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setClientSecret("secret_123");
        when(stripeService.createPaymentIntent(42.50)).thenReturn(mockIntent);

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 42.50);

        // WHEN
        ResponseEntity<Map<String, String>> response =
            stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("secret_123", response.getBody().get("clientSecret"));
        verify(stripeService).createPaymentIntent(42.50);
    }

    @Test
    void createPaymentIntent_shouldReturn500_onStripeException() throws Exception {
        // GIVEN : mock d'une StripeException
        StripeException stripeEx = mock(StripeException.class);
        when(stripeService.createPaymentIntent(anyDouble()))
            .thenThrow(stripeEx);

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10);

        // WHEN
        ResponseEntity<Map<String, String>> response =
            stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").startsWith("Erreur Stripe"));
    }

    @Test
    void createPaymentIntent_shouldReturn400_onInvalidAmount() {
        // GIVEN : un montant non convertible en Double
        Map<String, Object> request = new HashMap<>();
        request.put("amount", "pas un nombre");

        // WHEN
        ResponseEntity<Map<String, String>> response =
            stripeController.createPaymentIntent(request);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").startsWith("Données invalides"));
    }
}