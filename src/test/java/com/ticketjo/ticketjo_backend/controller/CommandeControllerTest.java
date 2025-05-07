package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.mapper.CommandeMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
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
    void creerCommande_shouldReturnCreatedCommande() {
        // Préparation du DTO d'entrée
        CommandeDTO dto = new CommandeDTO();
        dto.setDateCommande(LocalDate.now());
        dto.setStatut("EN_ATTENTE");
        dto.setTotalCommande(100.0);
        dto.setTickets(Collections.emptyList());

        // Conversion en entité et simulation du service
        Commande toCreate = CommandeMapper.toEntity(dto);
        Commande created = new Commande();
        created.setIdCommande(1L);
        created.setDateCommande(toCreate.getDateCommande());
        created.setStatutCommande(toCreate.getStatutCommande());
        created.setTotalCommande(toCreate.getTotalCommande());
        when(commandeService.creerCommande(any(Commande.class))).thenReturn(created);

        // Appel du contrôleur
        ResponseEntity<CommandeDTO> response = commandeController.creerCommande(dto);

        // Assertions
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getIdCommande());
        assertEquals("EN_ATTENTE", response.getBody().getStatut());
        assertEquals(100.0, response.getBody().getTotalCommande());
    }

    @Test
    void listerCommandesUtilisateur_shouldReturnList() {
        Long userId = 42L;
        Commande cmd = new Commande();
        cmd.setIdCommande(2L);
        cmd.setDateCommande(LocalDate.of(2025, 1, 1));
        cmd.setStatutCommande("VALIDEE");
        cmd.setTotalCommande(50.0);
        when(commandeService.listerCommandesUtilisateur(userId)).thenReturn(List.of(cmd));

        ResponseEntity<List<CommandeDTO>> response =
            commandeController.listerCommandesUtilisateur(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        CommandeDTO dto = response.getBody().get(0);
        assertEquals(2L, dto.getIdCommande());
        assertEquals("VALIDEE", dto.getStatut());
    }

    @Test
    void obtenirDerniereCommandeUtilisateur_shouldReturnCommande_whenExists() {
        Long userId = 42L;
        Commande cmd = new Commande();
        cmd.setIdCommande(3L);
        cmd.setDateCommande(LocalDate.of(2025, 2, 2));
        cmd.setStatutCommande("LIVREE");
        cmd.setTotalCommande(75.0);
        when(commandeService.obtenirDerniereCommandeUtilisateur(userId)).thenReturn(cmd);

        ResponseEntity<CommandeDTO> response =
            commandeController.obtenirDerniereCommandeUtilisateur(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getIdCommande());
        assertEquals("LIVREE", response.getBody().getStatut());
    }

    @Test
    void obtenirDerniereCommandeUtilisateur_shouldReturnNotFound_whenNone() {
        Long userId = 99L;
        when(commandeService.obtenirDerniereCommandeUtilisateur(userId)).thenReturn(null);

        ResponseEntity<CommandeDTO> response =
            commandeController.obtenirDerniereCommandeUtilisateur(userId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void listerCommandesParStatut_shouldReturnList() {
        String statut = "EN_ATTENTE";
        Commande cmd = new Commande();
        cmd.setIdCommande(4L);
        cmd.setDateCommande(LocalDate.now());
        cmd.setStatutCommande(statut);
        cmd.setTotalCommande(30.0);
        when(commandeService.listerCommandesParStatut(statut)).thenReturn(List.of(cmd));

        ResponseEntity<List<CommandeDTO>> response =
            commandeController.listerCommandesParStatut(statut);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(statut, response.getBody().get(0).getStatut());
    }
}