package com.ticketjo.ticketjo_backend.controller;
import com.ticketjo.ticketjo_backend.dto.EvenementDTO;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.service.EvenementService;
import com.ticketjo.ticketjo_backend.mapper.EvenementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvenementControllerTest {

    @Mock
    private EvenementService evenementService;

    @InjectMocks
    private EvenementController evenementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerEvenement_shouldReturnCreatedEvenement() {
        EvenementDTO dto = new EvenementDTO(null, "Marathon", "Sport", LocalDate.now(), "Paris", "Course à pied", "marathon.jpg");

        Evenement entity = EvenementMapper.toEntity(dto);
        entity.setIdEvenement(1L);

        when(evenementService.creerEvenement(any(Evenement.class))).thenReturn(entity);

        ResponseEntity<EvenementDTO> response = evenementController.creerEvenement(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Marathon", response.getBody().getNomEvenement());
        assertEquals("marathon.jpg", response.getBody().getImageUrl());
    }

    @Test
    void rechercherParNom_shouldReturnMatchingEvents() {
        String nom = "Marathon";
        Evenement evenement = new Evenement(1L, nom, "Sport", LocalDate.now(), "Paris", "Compétition", "img1.jpg");
        when(evenementService.rechercherParNom(nom)).thenReturn(List.of(evenement));

        ResponseEntity<List<EvenementDTO>> response = evenementController.rechercherParNom(nom);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(nom, response.getBody().get(0).getNomEvenement());
        assertEquals("img1.jpg", response.getBody().get(0).getImageUrl());
    }

    @Test
    void rechercherParDiscipline_shouldReturnMatchingEvents() {
        String discipline = "Musique";
        Evenement event = new Evenement(2L, "Festival", discipline, LocalDate.now(), "Lyon", null, null);
        when(evenementService.rechercherParDiscipline(discipline)).thenReturn(List.of(event));

        ResponseEntity<List<EvenementDTO>> response = evenementController.rechercherParDiscipline(discipline);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(discipline, response.getBody().get(0).getDiscipline());
    }

    @Test
    void rechercherParDate_shouldReturnMatchingEvents() {
        LocalDate date = LocalDate.of(2025, 5, 6);
        String dateStr = date.toString();

        Evenement event = new Evenement(3L, "Expo", "Art", date, "Nice", null, null);
        when(evenementService.rechercherParDate(date)).thenReturn(List.of(event));

        ResponseEntity<List<EvenementDTO>> response = evenementController.rechercherParDate(dateStr);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(date, response.getBody().get(0).getDateEvenement());
    }

    @Test
    void rechercherParLieu_shouldReturnMatchingEvents() {
        String lieu = "Toulouse";
        Evenement event = new Evenement(4L, "Salon", "Tech", LocalDate.now(), lieu, null, null);
        when(evenementService.rechercherParLieu(lieu)).thenReturn(List.of(event));

        ResponseEntity<List<EvenementDTO>> response = evenementController.rechercherParLieu(lieu);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lieu, response.getBody().get(0).getLieuEvenement());
    }
}
