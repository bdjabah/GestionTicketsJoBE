package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.TicketDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.service.TicketService;
import com.ticketjo.ticketjo_backend.mapper.TicketMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    private final TicketService ticketService = mock(TicketService.class);
    private final TicketController controller = new TicketController(ticketService);

    private Ticket getSampleTicket() {
        Ticket ticket = new Ticket();
        ticket.setIdTicket(1L);
        ticket.setCleTicket("cle1234567890");
        ticket.setQrCode("qrcode123");
        ticket.setTypeTicket("VIP");
        ticket.setPrixTicket(150.0);
        ticket.setDateEvenement(LocalDate.now());
        return ticket;
    }

    @Test
    void testCreateTicket() {
        Ticket ticket = getSampleTicket();
        when(ticketService.creerTicket(any())).thenReturn(ticket);

        TicketDTO dto = TicketMapper.toDTO(ticket);
        ResponseEntity<TicketDTO> response = controller.createTicket(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("cle1234567890", response.getBody().getCleTicket());
    }

    @Test
    void testGetTicketsByCommande() {
        Commande commande = new Commande();
        commande.setIdCommande(10L);
        Ticket ticket = getSampleTicket();
        when(ticketService.obtenirTicketsParCommande(any())).thenReturn(List.of(ticket));

        ResponseEntity<List<TicketDTO>> response = controller.getTicketsByCommande(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetTicketsByUtilisateur() {
        Ticket ticket = getSampleTicket();
        when(ticketService.obtenirTicketsParUtilisateur(5L)).thenReturn(List.of(ticket));

        ResponseEntity<List<TicketDTO>> response = controller.getTicketsByUtilisateur(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("cle1234567890", response.getBody().get(0).getCleTicket());
    }

    @Test
    void testGetTicketsByEvenement() {
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(3L);
        Ticket ticket = getSampleTicket();
        when(ticketService.obtenirTicketsParEvenement(any())).thenReturn(List.of(ticket));

        ResponseEntity<List<TicketDTO>> response = controller.getTicketsByEvenement(3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetTicketByCle_Found() {
        Ticket ticket = getSampleTicket();
        when(ticketService.trouverTicketParCle("cle1234567890")).thenReturn(ticket);

        ResponseEntity<TicketDTO> response = controller.getTicketByCle("cle1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("VIP", response.getBody().getTypeTicket());
    }

    @Test
    void testGetTicketByCle_NotFound() {
        when(ticketService.trouverTicketParCle("absent")).thenReturn(null);

        ResponseEntity<TicketDTO> response = controller.getTicketByCle("absent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
