package com.ticketjo.ticketjo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketjo.ticketjo_backend.dto.TicketCatalogueDTO;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.service.TicketCatalogueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketCatalogueController.class)
@AutoConfigureMockMvc(addFilters = false) // désactive la sécurité

public class TicketCatalogueControllerTest {

    @Autowired
    private MockMvc mockMvc;
   
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private com.ticketjo.ticketjo_backend.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private com.ticketjo.ticketjo_backend.security.JwtUtil jwtUtil;

    @MockBean
    private TicketCatalogueService ticketCatalogueService;

    private TicketCatalogue ticket;
    private TicketCatalogueDTO ticketDTO;

    @BeforeEach
    public void setup() {
        ticket = new TicketCatalogue(1L, "Solo", 50.0, 10, 1, "solo.png");
        ticketDTO = new TicketCatalogueDTO();
        ticketDTO.setIdTicket(1L);
        ticketDTO.setTypeTicket("Solo");
        ticketDTO.setPrixTicket(50.0);
        ticketDTO.setStock(10);
        ticketDTO.setCapacite(1);
        ticketDTO.setImageTicket("solo.png");
    }

    @Test
    public void testGetAllTickets() throws Exception {
        when(ticketCatalogueService.obtenirTousLesTickets()).thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typeTicket").value("Solo"));
    }

    @Test
    public void testGetTicketById() throws Exception {
        when(ticketCatalogueService.getTicketById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeTicket").value("Solo"));
    }

    @Test
    public void testGetTicketById_NotFound() throws Exception {
        when(ticketCatalogueService.getTicketById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tickets/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAvailableTickets() throws Exception {
        when(ticketCatalogueService.obtenirTicketsDisponibles()).thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/tickets/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stock").value(10));
    }

    @Test
    public void testSearchByType() throws Exception {
        when(ticketCatalogueService.rechercherParTypeTicket("solo")).thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/tickets/recherche").param("type", "solo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typeTicket").value("Solo"));
    }

    @Test
    public void testDeleteTicket() throws Exception {
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(ticketCatalogueService).supprimerTicket(1L);
    }

    // Tu peux aussi tester updateTicket et createTicket avec MockMultipartFile
    @Test
    public void testCreateTicket() throws Exception {
        // Simule un fichier image
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "image-content".getBytes());

        // Simule la partie JSON, mais sous forme de chaîne, pas de bytes !
        MockMultipartFile ticketJson = new MockMultipartFile(
                "ticket", "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(ticketDTO).getBytes()); // <- ici la vraie correction

        // Mock du service
        when(ticketCatalogueService.creerTicket(any())).thenReturn(ticket);

        // Effectue la requête POST
        mockMvc.perform(multipart("/api/tickets")
                        .file(ticketJson)
                        .file(image))
                .andExpect(status().isCreated());
    }
}