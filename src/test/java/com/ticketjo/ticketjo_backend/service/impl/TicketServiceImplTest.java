package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.repository.TicketRepository;
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
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setCleTicket("cle123");
    }

    @Test
    void testCreerTicket_ShouldSaveTicket() {
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        Ticket result = ticketService.creerTicket(ticket);

        assertNotNull(result);
        assertEquals("cle123", result.getCleTicket());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testObtenirTicketsParCommande() {
        Commande commande = new Commande();
        when(ticketRepository.findByCommande(commande)).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.obtenirTicketsParCommande(commande);

        assertEquals(1, tickets.size());
        verify(ticketRepository).findByCommande(commande);
    }

    @Test
    void testObtenirTicketsParUtilisateur() {
        Long userId = 99L;
        when(ticketRepository.findByCommande_Utilisateur_IdUtilisateur(userId)).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.obtenirTicketsParUtilisateur(userId);

        assertEquals(1, tickets.size());
        verify(ticketRepository).findByCommande_Utilisateur_IdUtilisateur(userId);
    }

    @Test
    void testObtenirTicketsParEvenement() {
        Evenement evenement = new Evenement();
        when(ticketRepository.findByEvenement(evenement)).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.obtenirTicketsParEvenement(evenement);

        assertEquals(1, tickets.size());
        verify(ticketRepository).findByEvenement(evenement);
    }

    @Test
    void testTrouverTicketParCle() {
        when(ticketRepository.findByCleTicket("cle123")).thenReturn(ticket);

        Ticket found = ticketService.trouverTicketParCle("cle123");

        assertNotNull(found);
        assertEquals("cle123", found.getCleTicket());
        verify(ticketRepository).findByCleTicket("cle123");
    }
}