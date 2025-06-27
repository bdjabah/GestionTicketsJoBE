package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.config.StripeProperties;
import com.ticketjo.ticketjo_backend.dto.PaiementDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.service.PaiementService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaiementControllerTest {

    @Mock
    private PaiementService paiementService;

    @Mock
    private StripeProperties stripeProperties;

    @Mock
    private CommandeRepository commandeRepository;

    @InjectMocks
    private PaiementController paiementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerPaiement_shouldReturnCreatedPaiement() {
        // Préparation du DTO envoyé par le frontend
        PaiementDTO dto = new PaiementDTO();
        dto.setStatut(StatutPaiement.EN_ATTENTE.name());
        dto.setMontant(100.0);
        dto.setDatePaiement(LocalDate.of(2025, 5, 1));
        dto.setMethodePaiement("Carte");
        dto.setIdCommande(1L);
        dto.setPaymentIntentId("pi_123");

        // Stub de la méthode creerPaiement du service
        Paiement saved = new Paiement();
        saved.setIdPaiement(10L);
        saved.setStatut(StatutPaiement.EN_ATTENTE);
        saved.setMontantPaiement(100.0);
        saved.setDatePaiement(dto.getDatePaiement());
        saved.setMethodePaiement("Carte");
        // On stubpe une commande minimale
        Commande commandeStub = new Commande();
        commandeStub.setIdCommande(1L);
        saved.setCommande(commandeStub);
        saved.setPaymentIntentId("pi_123");
        when(paiementService.creerPaiement(any(Paiement.class))).thenReturn(saved);

        // Exécution de la méthode du controller
        ResponseEntity<?> response = paiementController.creerPaiement(dto);

        // Vérifications
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof PaiementDTO);

        PaiementDTO body = (PaiementDTO) response.getBody();
        assertNotNull(body);
        assertEquals(10L, body.getIdPaiement());
        assertEquals(StatutPaiement.EN_ATTENTE.name(), body.getStatut());
        assertEquals(100.0, body.getMontant());
        assertEquals("Carte", body.getMethodePaiement());
        assertEquals(1L, body.getIdCommande());
        assertEquals("pi_123", body.getPaymentIntentId());
    }
    @Test
    void trouverParCommande_found() {
        Long idCommande = 2L;
        Paiement p = new Paiement();
        p.setIdPaiement(20L);
        p.setStatut(StatutPaiement.VALIDE);
        p.setMontantPaiement(50.0);
        p.setDatePaiement(LocalDate.of(2025, 5, 2));
        when(paiementService.trouverParCommande(idCommande)).thenReturn(p);

        ResponseEntity<PaiementDTO> response = paiementController.trouverParCommande(idCommande);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(20L, response.getBody().getIdPaiement());
        assertEquals(StatutPaiement.VALIDE.name(), response.getBody().getStatut());
    }

    @Test
    void trouverParCommande_notFound() {
        when(paiementService.trouverParCommande(anyLong())).thenReturn(null);

        ResponseEntity<PaiementDTO> response = paiementController.trouverParCommande(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void listerPaiementsParStatut_shouldReturnList() {
        StatutPaiement statut = StatutPaiement.ECHOUE;
        Paiement p = new Paiement();
        p.setIdPaiement(30L);
        p.setStatut(statut);
        p.setMontantPaiement(25.0);
        p.setDatePaiement(LocalDate.of(2025, 5, 3));
        when(paiementService.listerPaiementsParStatut(statut)).thenReturn(List.of(p));

        ResponseEntity<List<PaiementDTO>> response = paiementController.listerPaiementsParStatut(statut);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(statut.name(), response.getBody().get(0).getStatut());
    }

    @Test
    void listerPaiementsUtilisateur_shouldReturnList() {
        Long idUtilisateur = 5L;
        Paiement p = new Paiement();
        p.setIdPaiement(40L);
        p.setStatut(StatutPaiement.VALIDE);
        p.setMontantPaiement(75.0);
        when(paiementService.listerPaiementsUtilisateur(idUtilisateur)).thenReturn(List.of(p));

        ResponseEntity<List<PaiementDTO>> response = paiementController.listerPaiementsUtilisateur(idUtilisateur);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(40L, response.getBody().get(0).getIdPaiement());
    }

    @Test
    void getPaiementByIntentId_found() {
        String intentId = "pi_456";
        Paiement p = new Paiement();
        p.setIdPaiement(50L);
        p.setPaymentIntentId(intentId);
        p.setStatut(StatutPaiement.VALIDE);
        when(paiementService.trouverParIntentId(intentId)).thenReturn(p);

        ResponseEntity<PaiementDTO> response = paiementController.getPaiementByIntentId(intentId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(50L, response.getBody().getIdPaiement());
        assertEquals(intentId, response.getBody().getPaymentIntentId());
    }

    @Test
    void getPaiementByIntentId_notFound() {
        when(paiementService.trouverParIntentId(anyString())).thenReturn(null);

        ResponseEntity<PaiementDTO> response = paiementController.getPaiementByIntentId("inexistant");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void handleStripeWebhook_noSignature_shouldReturnBadRequest() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Stripe-Signature")).thenReturn(null);
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader("payload")));

        ResponseEntity<String> response = paiementController.handleStripeWebhook(req);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Signature header manquant", response.getBody());
    }
}