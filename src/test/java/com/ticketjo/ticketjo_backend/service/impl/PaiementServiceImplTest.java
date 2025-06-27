package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.repository.PaiementRepository;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaiementServiceImplTest {

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private CommandeRepository commandeRepository;

    @InjectMocks
    private PaiementServiceImpl paiementService;

    private Paiement paiement;
    private Commande commande;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(42L);

        commande = new Commande();
        commande.setIdCommande(1L);
        commande.setUtilisateur(utilisateur);

        paiement = new Paiement();
        paiement.setCommande(commande);
        paiement.setMontantPaiement(150.0);
        paiement.setStatut(StatutPaiement.VALIDE);
    }

    void testCreerPaiement() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(paiementRepository.save(any(Paiement.class))).thenAnswer(i -> i.getArgument(0));

        Paiement result = paiementService.creerPaiement(paiement);

        assertNotNull(result);
        assertEquals(150.0, result.getMontantPaiement());
        assertEquals(commande, result.getCommande());
        assertEquals(utilisateur, result.getUtilisateur());
        verify(paiementRepository).save(any(Paiement.class));
    }
    @Test
    void testTrouverParCommande_WhenCommandeExists() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(paiementRepository.findByCommande(commande)).thenReturn(paiement);

        Paiement result = paiementService.trouverParCommande(1L);

        assertNotNull(result);
        assertEquals(commande, result.getCommande());
    }

    @Test
    void testTrouverParCommande_WhenCommandeNotFound() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.empty());

        Paiement result = paiementService.trouverParCommande(1L);

        assertNull(result);
    }

    @Test
    void testListerPaiementsParStatut() {
        when(paiementRepository.findByStatut(StatutPaiement.VALIDE)).thenReturn(List.of(paiement));

        List<Paiement> paiements = paiementService.listerPaiementsParStatut(StatutPaiement.VALIDE);

        assertEquals(1, paiements.size());
        verify(paiementRepository).findByStatut(StatutPaiement.VALIDE);
    }

    @Test
    void testListerPaiementsUtilisateur() {
        Long userId = 99L;
        when(paiementRepository.findByCommande_Utilisateur_IdUtilisateur(userId)).thenReturn(List.of(paiement));

        List<Paiement> result = paiementService.listerPaiementsUtilisateur(userId);

        assertEquals(1, result.size());
        verify(paiementRepository).findByCommande_Utilisateur_IdUtilisateur(userId);
    }
}