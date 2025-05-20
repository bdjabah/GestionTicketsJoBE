package com.ticketjo.ticketjo_backend.controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ticketjo.ticketjo_backend.dto.TicketDTO;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
public class TicketControllerTest {

    // Dépendances simulées (mockées)
    private TicketController controller;
    private TicketService ticketService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Initialisation des mocks et du contrôleur avant chaque test
        ticketService = mock(TicketService.class);
        objectMapper = new ObjectMapper();
        controller = new TicketController(ticketService, objectMapper);
    }

    @Test
    public void testCreateTicketSansImage() throws Exception {
        // Préparation des données d'entrée (DTO simulé)
        TicketDTO dto = new TicketDTO();
        dto.setTypeTicket("VIP");
        dto.setPrixTicket(150.0);
        dto.setStock(100);
        dto.setCleTicket("CLE123456789");
        dto.setQrCode("QR123456789");
        dto.setIdCommande(1L);
        dto.setIdEvenement(2L);

        // Réponse simulée du service (Ticket simulé)
        Ticket fakeTicket = new Ticket();
        fakeTicket.setTypeTicket("VIP");
        fakeTicket.setPrixTicket(150.0);
        fakeTicket.setStock(100);
        fakeTicket.setCleTicket("CLE123456789");
        fakeTicket.setQrCode("QR123456789");
        when(ticketService.creerTicket(any())).thenReturn(fakeTicket);

        // Conversion du DTO en JSON comme attendu dans la requête multipart
        String json = objectMapper.writeValueAsString(dto);

        // Appel du contrôleur avec une image nulle (test sans image)
        ResponseEntity<TicketDTO> response = controller.createTicket(json, null);

        // Assertions sur le statut HTTP et la réponse
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("VIP", response.getBody().getTypeTicket());
    }

    @Test
    public void testDeleteTicket() {
        // Simule une suppression sans erreur
        doNothing().when(ticketService).supprimerTicket(1L);

        // Appel du contrôleur
        ResponseEntity<Void> response = controller.deleteTicket(1L);

        // Vérifie le statut HTTP et que la méthode a bien été appelée
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService, times(1)).supprimerTicket(1L);
    }

    @Test
    public void testGetTicketsByCommande() {
        // Préparation d'une commande et d'un ticket fictif
        Commande commande = new Commande();
        commande.setIdCommande(1L);
        Ticket ticket = new Ticket();
        ticket.setTypeTicket("STANDARD");

        // Simule la réponse du service
        when(ticketService.obtenirTicketsParCommande(any())).thenReturn(List.of(ticket));

        // Appel du contrôleur
        ResponseEntity<List<TicketDTO>> response = controller.getTicketsByCommande(1L);

        // Vérification de la réponse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("STANDARD", response.getBody().get(0).getTypeTicket());
    }

    @Test
    public void testGetTicketsByUtilisateur() {
        // Ticket fictif lié à un utilisateur
        Ticket ticket = new Ticket();
        ticket.setTypeTicket("VIP");

        // Simule le retour du service
        when(ticketService.obtenirTicketsParUtilisateur(2L)).thenReturn(List.of(ticket));

        // Appel du contrôleur
        ResponseEntity<List<TicketDTO>> response = controller.getTicketsByUtilisateur(2L);

        // Vérification du résultat
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("VIP", response.getBody().get(0).getTypeTicket());
    }

    @Test
    public void testGetTicketsByEvenement() {
        // Événement fictif
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(3L);

        // Ticket fictif lié à l'événement
        Ticket ticket = new Ticket();
        ticket.setTypeTicket("PREMIUM");

        // Simule la réponse du service
        when(ticketService.obtenirTicketsParEvenement(any())).thenReturn(List.of(ticket));

        // Appel du contrôleur
        ResponseEntity<List<TicketDTO>> response = controller.getTicketsByEvenement(3L);

        // Vérification de la réponse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PREMIUM", response.getBody().get(0).getTypeTicket());
    }

    @Test
    public void testGetTicketByCle_found() {
        // Ticket fictif avec clé valide
        Ticket ticket = new Ticket();
        ticket.setCleTicket("ABC1234567");
        ticket.setTypeTicket("VIP");

        // Simule la recherche réussie
        when(ticketService.trouverTicketParCle("ABC1234567")).thenReturn(ticket);

        // Appel du contrôleur
        ResponseEntity<TicketDTO> response = controller.getTicketByCle("ABC1234567");

        // Vérification de la réponse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("VIP", response.getBody().getTypeTicket());
    }

    @Test
    public void testGetTicketByCle_notFound() {
        // Simule une recherche infructueuse
        when(ticketService.trouverTicketParCle("INVALID")).thenReturn(null);

        // Appel du contrôleur
        ResponseEntity<TicketDTO> response = controller.getTicketByCle("INVALID");

        // Vérifie que la réponse est bien 404
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllTickets() {
        // Ticket fictif
        Ticket ticket = new Ticket();
        ticket.setTypeTicket("VIP");

        // Simule la récupération de tous les tickets
        when(ticketService.obtenirTousLesTickets()).thenReturn(List.of(ticket));

        // Appel du contrôleur
        ResponseEntity<List<TicketDTO>> response = controller.getAllTickets();

        // Vérification du contenu et du statut
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("VIP", response.getBody().get(0).getTypeTicket());
    }

}