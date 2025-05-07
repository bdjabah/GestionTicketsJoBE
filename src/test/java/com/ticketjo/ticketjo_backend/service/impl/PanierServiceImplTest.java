package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Panier;
import com.ticketjo.ticketjo_backend.repository.PanierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PanierServiceImplTest {

    @Mock
    private PanierRepository panierRepository;

    @InjectMocks
    private PanierServiceImpl panierService;

    private Panier panier;

    @BeforeEach
    void setUp() {
        panier = new Panier();
        panier.setStatutPanier("EN_COURS");
    }

    @Test
    void testAjouterPanier() {
        when(panierRepository.save(panier)).thenReturn(panier);

        Panier result = panierService.ajouterPanier(panier);

        assertNotNull(result);
        assertEquals("EN_COURS", result.getStatutPanier());
        verify(panierRepository).save(panier);
    }

    @Test
    void testSupprimerPanier() {
        Long idPanier = 10L;

        panierService.supprimerPanier(idPanier);

        verify(panierRepository).deleteById(idPanier);
    }

    @Test
    void testListerPaniersParUtilisateur() {
        Long idUtilisateur = 1L;
        when(panierRepository.findByUtilisateur_IdUtilisateur(idUtilisateur)).thenReturn(List.of(panier));

        List<Panier> result = panierService.listerPaniersParUtilisateur(idUtilisateur);

        assertEquals(1, result.size());
        verify(panierRepository).findByUtilisateur_IdUtilisateur(idUtilisateur);
    }
}