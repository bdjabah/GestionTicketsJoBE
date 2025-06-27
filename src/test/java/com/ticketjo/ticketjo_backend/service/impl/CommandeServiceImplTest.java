package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
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

    @Test
    void testCreerCommande() {
        CommandeDTO commandeDTO = new CommandeDTO();
        commandeDTO.setDateCommande(LocalDate.now());
        commandeDTO.setTotalCommande(100.0);
        commandeDTO.setStatut(StatutCommande.EN_ATTENTE);
        commandeDTO.setIdUtilisateur(null); // pas de user pour ce test
        commandeDTO.setTickets(List.of()); // pas de billets non plus pour ce test simple

        // Stub du repository
        when(commandeRepository.save(any(Commande.class))).thenAnswer(invocation -> {
            Commande c = invocation.getArgument(0);
            c.setIdCommande(1L);
            return c;
        });

        Commande result = commandeService.creerCommande(commandeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdCommande());
        assertEquals(StatutCommande.EN_ATTENTE, result.getStatutCommande());
        verify(commandeRepository).save(any(Commande.class));
    }

    @Test
    void testListerCommandesUtilisateur() {
        Long idUtilisateur = 2L;
        Commande commande = new Commande();
        when(commandeRepository.findByUtilisateur_IdUtilisateur(idUtilisateur))
                .thenReturn(List.of(commande));

        List<Commande> commandes = commandeService.listerCommandesUtilisateur(idUtilisateur);

        assertEquals(1, commandes.size());
        verify(commandeRepository).findByUtilisateur_IdUtilisateur(idUtilisateur);
    }

    @Test
    void testObtenirDerniereCommandeUtilisateur() {
        Long idUtilisateur = 2L;
        Commande commande = new Commande();
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
        Commande commande = new Commande();
        when(commandeRepository.findByStatutCommande(StatutCommande.TERMINEE))
                .thenReturn(List.of(commande));

        List<Commande> result = commandeService.listerCommandesParStatut("terminee");

        assertEquals(1, result.size());
        verify(commandeRepository).findByStatutCommande(StatutCommande.TERMINEE);
    }
}