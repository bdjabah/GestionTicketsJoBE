package com.ticketjo.ticketjo_backend.controller;


import com.ticketjo.ticketjo_backend.dto.PanierDTO;
import com.ticketjo.ticketjo_backend.mapper.PanierMapper;
import com.ticketjo.ticketjo_backend.model.Panier;
import com.ticketjo.ticketjo_backend.service.PanierService;
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

class PanierControllerTest {

    @Mock
    private PanierService panierService;

    @InjectMocks
    private PanierController panierController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ajouterPanier_shouldReturnCreatedPanier() {
        PanierDTO dto = new PanierDTO();
        dto.setDateCreation(LocalDate.now());
        dto.setStatutPanier("EN_COURS");
        dto.setIdUtilisateur(1L);

        Panier mockPanier = new Panier();
        mockPanier.setIdPanier(1L);
        mockPanier.setDateCreation(dto.getDateCreation());
        mockPanier.setStatutPanier(dto.getStatutPanier());

        when(panierService.ajouterPanier(any(Panier.class))).thenReturn(mockPanier);

        ResponseEntity<PanierDTO> response = panierController.ajouterPanier(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EN_COURS", response.getBody().getStatutPanier());
    }

    @Test
    void supprimerPanier_shouldReturnNoContent() {
        Long id = 42L;

        doNothing().when(panierService).supprimerPanier(id);

        ResponseEntity<Void> response = panierController.supprimerPanier(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(panierService, times(1)).supprimerPanier(id);
    }

    @Test
    void listerPaniersParUtilisateur_shouldReturnList() {
        Long userId = 10L;
        Panier mockPanier = new Panier();
        mockPanier.setIdPanier(1L);
        mockPanier.setStatutPanier("EN_COURS");

        when(panierService.listerPaniersParUtilisateur(userId)).thenReturn(Collections.singletonList(mockPanier));

        ResponseEntity<List<PanierDTO>> response = panierController.listerPaniersParUtilisateur(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("EN_COURS", response.getBody().get(0).getStatutPanier());
    }
}