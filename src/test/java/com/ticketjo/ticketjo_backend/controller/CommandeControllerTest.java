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
    void creerCommande_shouldReturnCreatedCommande() {
        // Préparation du DTO d'entrée
        CommandeDTO dto = new CommandeDTO();
        dto.setDateCommande(LocalDate.now());
        dto.setStatut(StatutCommande.EN_ATTENTE); // Ne sera pas utilisé, mais on le met pour le test
        dto.setTotalCommande(150.0);
        dto.setIdUtilisateur(42L); // nouveau champ à tester
        dto.setTickets(Collections.emptyList());

        // Conversion en entité et simulation du service
        Commande toCreate = CommandeMapper.toEntity(dto);

        // Le service impose EN_ATTENTE quoi qu'on passe
        Commande created = new Commande();
        created.setIdCommande(100L);
        created.setDateCommande(toCreate.getDateCommande());
        created.setStatutCommande(StatutCommande.EN_ATTENTE); // forcé dans le service
        created.setTotalCommande(toCreate.getTotalCommande());
        Utilisateur fakeUser = new Utilisateur();
        fakeUser.setIdUtilisateur(42L);
        created.setUtilisateur(fakeUser);

        when(commandeService.creerCommande(any(Commande.class))).thenReturn(created);

        // Appel du contrôleur
        ResponseEntity<CommandeDTO> response = commandeController.creerCommande(dto);

        // Assertions
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getIdCommande());
        assertEquals(StatutCommande.EN_ATTENTE, response.getBody().getStatut()); // Vérifie bien la valeur imposée
        assertEquals(150.0, response.getBody().getTotalCommande());
        assertEquals(42L, response.getBody().getIdUtilisateur());
    }

    @Test
    void listerCommandesUtilisateur_shouldReturnList() {
        Long userId = 7L;
        Commande cmd = new Commande();
        cmd.setIdCommande(2L);
        cmd.setDateCommande(LocalDate.of(2025, 3, 15));
        cmd.setStatutCommande(StatutCommande.EN_PREPARATION);
        cmd.setTotalCommande(45.5);
        when(commandeService.listerCommandesUtilisateur(userId)).thenReturn(List.of(cmd));

        ResponseEntity<List<CommandeDTO>> response =
            commandeController.listerCommandesUtilisateur(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        CommandeDTO returned = response.getBody().get(0);
        assertEquals(2L, returned.getIdCommande());
        assertEquals(StatutCommande.EN_PREPARATION, returned.getStatut());
        assertEquals(45.5, returned.getTotalCommande());
    }

    @Test
    void obtenirDerniereCommandeUtilisateur_shouldReturnCommande_whenExists() {
        Long userId = 8L;
        Commande cmd = new Commande();
        cmd.setIdCommande(5L);
        cmd.setDateCommande(LocalDate.of(2025, 4, 20));
        cmd.setStatutCommande(StatutCommande.TERMINEE);
        cmd.setTotalCommande(80.0);
        when(commandeService.obtenirDerniereCommandeUtilisateur(userId)).thenReturn(cmd);

        ResponseEntity<CommandeDTO> response =
            commandeController.obtenirDerniereCommandeUtilisateur(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(5L, response.getBody().getIdCommande());
        assertEquals(StatutCommande.TERMINEE, response.getBody().getStatut());
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
        StatutCommande statut = StatutCommande.EN_COURS;
        Commande cmd = new Commande();
        cmd.setIdCommande(4L);
        cmd.setDateCommande(LocalDate.now());
        cmd.setStatutCommande(statut);
        cmd.setTotalCommande(30.0);
        when(commandeService.listerCommandesParStatut(statut.name())).thenReturn(List.of(cmd));

        ResponseEntity<List<CommandeDTO>> response =
            commandeController.listerCommandesParStatut(statut.name());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(statut, response.getBody().get(0).getStatut());
    }

    @Test
    void changerStatut_shouldReturnUpdatedCommande() {
        Long commandeId = 15L;
        StatutCommande nouveauStatut = StatutCommande.ANNULEE;

        Commande updated = new Commande();
        updated.setIdCommande(commandeId);
        updated.setDateCommande(LocalDate.of(2025, 5, 1));
        updated.setStatutCommande(nouveauStatut);
        updated.setTotalCommande(200.0);
        when(commandeService.changerStatut(eq(commandeId), eq(nouveauStatut)))
            .thenReturn(updated);

        ResponseEntity<CommandeDTO> response =
            commandeController.changerStatut(commandeId, nouveauStatut);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(commandeId, response.getBody().getIdCommande());
        assertEquals(nouveauStatut, response.getBody().getStatut());
        assertEquals(200.0, response.getBody().getTotalCommande());
    }
}