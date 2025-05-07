package com.ticketjo.ticketjo_backend.controller;
import com.ticketjo.ticketjo_backend.dto.PaiementDTO;
import com.ticketjo.ticketjo_backend.mapper.PaiementMapper;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.service.PaiementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaiementControllerTest {

    @Mock
    private PaiementService paiementService;

    @InjectMocks
    private PaiementController paiementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerPaiement_shouldReturnCreated() {
        PaiementDTO dto = new PaiementDTO();
        dto.setStatut("VALIDE");
        dto.setMontant(100.0);
        dto.setDatePaiement(LocalDate.now());
        dto.setMethodePaiement("CB");
        dto.setIdCommande(1L);

        Paiement paiement = PaiementMapper.toEntity(dto);
        Paiement saved = paiement;
        saved.setIdPaiement(99L);

        when(paiementService.creerPaiement(any())).thenReturn(saved);

        ResponseEntity<PaiementDTO> response = paiementController.creerPaiement(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDE", response.getBody().getStatut());
        verify(paiementService, times(1)).creerPaiement(any());
    }

    @Test
    void trouverParCommande_shouldReturnPaymentIfFound() {
        Paiement paiement = new Paiement();
        paiement.setIdPaiement(42L);
        paiement.setMontantPaiement(200.0);
        paiement.setStatut(StatutPaiement.EN_ATTENTE);

        when(paiementService.trouverParCommande(1L)).thenReturn(paiement);

        ResponseEntity<PaiementDTO> response = paiementController.trouverParCommande(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200.0, response.getBody().getMontant());
    }

    @Test
    void trouverParCommande_shouldReturnNotFoundIfMissing() {
        when(paiementService.trouverParCommande(1L)).thenReturn(null);

        ResponseEntity<PaiementDTO> response = paiementController.trouverParCommande(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void listerPaiementsParStatut_shouldReturnList() {
        Paiement paiement = new Paiement();
        paiement.setStatut(StatutPaiement.VALIDE);
        paiement.setMontantPaiement(150.0);

        when(paiementService.listerPaiementsParStatut(StatutPaiement.VALIDE))
                .thenReturn(List.of(paiement));

        ResponseEntity<List<PaiementDTO>> response = paiementController.listerPaiementsParStatut(StatutPaiement.VALIDE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void listerPaiementsUtilisateur_shouldReturnList() {
        Paiement paiement = new Paiement();
        paiement.setMontantPaiement(99.9);
        paiement.setStatut(StatutPaiement.EN_ATTENTE);

        when(paiementService.listerPaiementsUtilisateur(123L))
                .thenReturn(Collections.singletonList(paiement));

        ResponseEntity<List<PaiementDTO>> response = paiementController.listerPaiementsUtilisateur(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}