package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.repository.TicketCatalogueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketCatalogueServiceImplTest {

    @Mock
    private TicketCatalogueRepository ticketCatalogueRepository;

    @InjectMocks
    private TicketCatalogueServiceImpl ticketService;

    private TicketCatalogue ticketCatalogue;

    @BeforeEach
    void setUp() {
        ticketCatalogue = new TicketCatalogue();
        ticketCatalogue.setIdTicket(1L);
        ticketCatalogue.setTypeTicket("solo");
        ticketCatalogue.setPrixTicket(20.0);
        ticketCatalogue.setStock(10);
        ticketCatalogue.setCapacite(2);
        ticketCatalogue.setImageTicket("image.jpg");
    }

    @Test
    void testCreerTicket_ShouldSaveTicket() {
        when(ticketCatalogueRepository.save(ticketCatalogue)).thenReturn(ticketCatalogue);

        TicketCatalogue result = ticketService.creerTicket(ticketCatalogue);

        assertNotNull(result);
        assertEquals("solo", result.getTypeTicket());
        verify(ticketCatalogueRepository).save(ticketCatalogue);
    }

    @Test
    void testMettreAJourTicket_WhenExists_ShouldUpdate() {
        when(ticketCatalogueRepository.findById(1L)).thenReturn(Optional.of(ticketCatalogue));
        when(ticketCatalogueRepository.save(ticketCatalogue)).thenReturn(ticketCatalogue);

        TicketCatalogue updated = ticketService.mettreAJourTicket(ticketCatalogue);

        assertNotNull(updated);
        assertEquals(10, updated.getStock());
        verify(ticketCatalogueRepository).findById(1L);
        verify(ticketCatalogueRepository).save(ticketCatalogue);
    }

    @Test
    void testMettreAJourTicket_WhenNotExists_ShouldThrow() {
        when(ticketCatalogueRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketService.mettreAJourTicket(ticketCatalogue);
        });

        assertTrue(exception.getMessage().contains("Ticket non trouvé"));
        verify(ticketCatalogueRepository).findById(1L);
    }

    @Test
    void testSupprimerTicket_ShouldCallDelete() {
        ticketService.supprimerTicket(1L);
        verify(ticketCatalogueRepository).deleteById(1L);
    }

    @Test
    void testObtenirTousLesTickets_ShouldReturnList() {
        when(ticketCatalogueRepository.findAll()).thenReturn(Arrays.asList(ticketCatalogue));

        List<TicketCatalogue> ticketCatalogues = ticketService.obtenirTousLesTickets();

        assertEquals(1, ticketCatalogues.size());
        verify(ticketCatalogueRepository).findAll();
    }

    @Test
    void testGetTicketById_ShouldReturnOptional() {
        when(ticketCatalogueRepository.findById(1L)).thenReturn(Optional.of(ticketCatalogue));

        Optional<TicketCatalogue> result = ticketService.getTicketById(1L);

        assertTrue(result.isPresent());
        verify(ticketCatalogueRepository).findById(1L);
    }

    @Test
    void testObtenirTicketsDisponibles_ShouldReturnList() {
        when(ticketCatalogueRepository.findByStockGreaterThan(0)).thenReturn(List.of(ticketCatalogue));

        List<TicketCatalogue> disponibles = ticketService.obtenirTicketsDisponibles();

        assertEquals(1, disponibles.size());
        verify(ticketCatalogueRepository).findByStockGreaterThan(0);
    }

    @Test
    void testRechercherParTypeTicket_ShouldReturnList() {
        when(ticketCatalogueRepository.findByTypeTicketContainingIgnoreCase("solo")).thenReturn(List.of(ticketCatalogue));

        List<TicketCatalogue> found = ticketService.rechercherParTypeTicket("solo");

        assertEquals(1, found.size());
        verify(ticketCatalogueRepository).findByTypeTicketContainingIgnoreCase("solo");
    }
}

