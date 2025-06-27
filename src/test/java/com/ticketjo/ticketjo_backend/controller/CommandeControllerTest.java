package com.ticketjo.ticketjo_backend.controller;
import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.mapper.CommandeMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;
import com.ticketjo.ticketjo_backend.service.CommandeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommandeControllerTest {

    @Mock
    private CommandeService commandeService;

    @InjectMocks
    private CommandeController commandeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerCommande_retourneCommandeCreee() {
        CommandeDTO dto = new CommandeDTO();
        dto.setDateCommande(LocalDate.now());
        dto.setStatut(StatutCommande.PAYEE); // sera ignoré par le service
        dto.setTotalCommande(150.0);
        dto.setIdUtilisateur(42L);
        dto.setTickets(Collections.emptyList());

        Commande entity = CommandeMapper.toEntity(dto);
        entity.setIdCommande(1L);
        entity.setStatutCommande(StatutCommande.EN_ATTENTE);
        Utilisateur user = new Utilisateur();
        user.setIdUtilisateur(42L);
        entity.setUtilisateur(user);

        when(commandeService.creerCommande(any(CommandeDTO.class))).thenReturn(entity);

        ResponseEntity<CommandeDTO> response = commandeController.creerCommande(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getIdCommande());
        assertEquals(StatutCommande.EN_ATTENTE, response.getBody().getStatut());
    }

    @Test
    void listerCommandesUtilisateur_retourneListe() {
        Long userId = 10L;
        Commande cmd = new Commande();
        cmd.setIdCommande(2L);
        cmd.setDateCommande(LocalDate.now());
        cmd.setStatutCommande(StatutCommande.PRETE);
        cmd.setTotalCommande(45.5);

        when(commandeService.listerCommandesUtilisateur(userId)).thenReturn(List.of(cmd));

        ResponseEntity<List<CommandeDTO>> response = commandeController.listerCommandesUtilisateur(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(2L, response.getBody().get(0).getIdCommande());
    }

    @Test
    void obtenirDerniereCommandeUtilisateur_retourneCommande() {
        Long userId = 8L;
        Commande cmd = new Commande();
        cmd.setIdCommande(5L);
        cmd.setDateCommande(LocalDate.now());
        cmd.setStatutCommande(StatutCommande.TERMINEE);
        cmd.setTotalCommande(80.0);

        when(commandeService.obtenirDerniereCommandeUtilisateur(userId)).thenReturn(cmd);

        ResponseEntity<CommandeDTO> response = commandeController.obtenirDerniereCommandeUtilisateur(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(5L, response.getBody().getIdCommande());
        assertEquals(StatutCommande.TERMINEE, response.getBody().getStatut());
    }

    @Test
    void obtenirDerniereCommandeUtilisateur_retourneNotFound() {
        when(commandeService.obtenirDerniereCommandeUtilisateur(99L)).thenReturn(null);

        ResponseEntity<CommandeDTO> response = commandeController.obtenirDerniereCommandeUtilisateur(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void listerCommandesParStatut_retourneListe() {
        StatutCommande statut = StatutCommande.PAYEE;
        Commande cmd = new Commande();
        cmd.setIdCommande(4L);
        cmd.setDateCommande(LocalDate.now());
        cmd.setStatutCommande(statut);
        cmd.setTotalCommande(30.0);

        when(commandeService.listerCommandesParStatut(statut.name())).thenReturn(List.of(cmd));

        ResponseEntity<List<CommandeDTO>> response = commandeController.listerCommandesParStatut(statut.name());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(statut, response.getBody().get(0).getStatut());
    }

    @Test
    void changerStatut_retourneCommandeMiseAJour() {
        Long idCommande = 15L;
        StatutCommande nouveau = StatutCommande.VALIDE;

        Commande updated = new Commande();
        updated.setIdCommande(idCommande);
        updated.setDateCommande(LocalDate.now());
        updated.setStatutCommande(nouveau);
        updated.setTotalCommande(200.0);

        when(commandeService.changerStatut(eq(idCommande), eq(nouveau))).thenReturn(updated);

        ResponseEntity<?> response = commandeController.changerStatut(idCommande, nouveau);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof CommandeDTO);

        CommandeDTO dto = (CommandeDTO) response.getBody();
        assertEquals(idCommande, dto.getIdCommande());
        assertEquals(nouveau, dto.getStatut());
    }
}