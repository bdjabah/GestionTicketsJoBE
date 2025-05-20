package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceImplTest {

    @Mock
    private CommandeRepository commandeRepository;

    @InjectMocks
    private CommandeServiceImpl commandeService;

    private Commande commande;

    @BeforeEach
    void setUp() {
        commande = new Commande();
        commande.setIdCommande(1L);
        commande.setDateCommande(LocalDate.now());
        // on passe l'enum, pas une String
        commande.setStatutCommande(StatutCommande.TERMINEE);
    }

    @Test
    void testCreerCommande() {
        when(commandeRepository.save(commande)).thenReturn(commande);

        Commande result = commandeService.creerCommande(commande);

        assertNotNull(result);
        assertEquals(StatutCommande.TERMINEE, result.getStatutCommande());
        verify(commandeRepository).save(commande);
    }

    @Test
    void testListerCommandesUtilisateur() {
        Long idUtilisateur = 2L;
        when(commandeRepository.findByUtilisateur_IdUtilisateur(idUtilisateur))
            .thenReturn(List.of(commande));

        List<Commande> commandes = commandeService.listerCommandesUtilisateur(idUtilisateur);

        assertEquals(1, commandes.size());
        verify(commandeRepository).findByUtilisateur_IdUtilisateur(idUtilisateur);
    }

    @Test
    void testObtenirDerniereCommandeUtilisateur() {
        Long idUtilisateur = 2L;
        when(commandeRepository.findTopByUtilisateur_IdUtilisateurOrderByDateCommandeDesc(idUtilisateur))
            .thenReturn(Optional.of(commande));

        Commande result = commandeService.obtenirDerniereCommandeUtilisateur(idUtilisateur);

        assertNotNull(result);
        assertEquals(commande, result);
        verify(commandeRepository)
            .findTopByUtilisateur_IdUtilisateurOrderByDateCommandeDesc(idUtilisateur);
    }

    @Test
    void testListerCommandesParStatut() {
        // on stubbe le repo avec l'enum
        when(commandeRepository.findByStatutCommande(StatutCommande.TERMINEE))
            .thenReturn(List.of(commande));

        // on appelle la méthode service en passant une String (case-insensitive)
        List<Commande> result = commandeService.listerCommandesParStatut("terminee");

        assertEquals(1, result.size());
        // s'assure que l'on a bien converti en StatutCommande.TERMINEE
        verify(commandeRepository).findByStatutCommande(StatutCommande.TERMINEE);
    }
}