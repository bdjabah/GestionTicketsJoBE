package com.ticketjo.ticketjo_backend.service.impl;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.repository.EvenementRepository;
import org.junit.jupiter.api.BeforeEach;
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
class EvenementServiceImplTest {

    @Mock
    private EvenementRepository evenementRepository;

    @InjectMocks
    private EvenementServiceImpl evenementService;

    private Evenement evenement;

    @BeforeEach
    void setUp() {
        evenement = new Evenement();
        evenement.setIdEvenement(1L);
        evenement.setNomEvenement("100m Sprint");
        evenement.setDiscipline("Athlétisme");
        evenement.setDateEvenement(LocalDate.of(2024, 8, 1));
        evenement.setLieuEvenement("Paris");
    }

    @Test
    void testCreerEvenement() {
        when(evenementRepository.save(evenement)).thenReturn(evenement);

        Evenement result = evenementService.creerEvenement(evenement);

        assertNotNull(result);
        assertEquals("100m Sprint", result.getNomEvenement());
        verify(evenementRepository).save(evenement);
    }

    @Test
    void testRechercherParNom() {
        when(evenementRepository.findByNomEvenementContainingIgnoreCase("100m")).thenReturn(List.of(evenement));

        List<Evenement> results = evenementService.rechercherParNom("100m");

        assertEquals(1, results.size());
        verify(evenementRepository).findByNomEvenementContainingIgnoreCase("100m");
    }

    @Test
    void testRechercherParDiscipline() {
        when(evenementRepository.findByDisciplineContainingIgnoreCase("athl")).thenReturn(List.of(evenement));

        List<Evenement> results = evenementService.rechercherParDiscipline("athl");

        assertEquals(1, results.size());
        verify(evenementRepository).findByDisciplineContainingIgnoreCase("athl");
    }

    @Test
    void testRechercherParDate() {
        LocalDate date = LocalDate.of(2024, 8, 1);
        when(evenementRepository.findByDateEvenement(date)).thenReturn(List.of(evenement));

        List<Evenement> results = evenementService.rechercherParDate(date);

        assertEquals(1, results.size());
        verify(evenementRepository).findByDateEvenement(date);
    }

    @Test
    void testRechercherParLieu() {
        when(evenementRepository.findByLieuEvenementContainingIgnoreCase("Paris")).thenReturn(List.of(evenement));

        List<Evenement> results = evenementService.rechercherParLieu("Paris");

        assertEquals(1, results.size());
        verify(evenementRepository).findByLieuEvenementContainingIgnoreCase("Paris");
    }

    @Test
    void testUpdateEvenement() {
        // Cas de mise à jour valide
        when(evenementRepository.findById(1L)).thenReturn(Optional.of(evenement));
        when(evenementRepository.save(evenement)).thenReturn(evenement);

        Evenement updated = evenementService.updateEvenement(evenement);

        assertNotNull(updated);
        assertEquals("100m Sprint", updated.getNomEvenement());
        verify(evenementRepository).save(evenement);
    }

    @Test
    void testUpdateEvenement_NotFound() {
        // Cas d’échec : événement inexistant
        when(evenementRepository.findById(99L)).thenReturn(Optional.empty());
        evenement.setIdEvenement(99L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            evenementService.updateEvenement(evenement);
        });

        assertEquals("Événement introuvable avec l'ID 99", exception.getMessage());
    }

    @Test
    void testSupprimerEvenement() {
        evenementService.supprimerEvenement(1L);
        verify(evenementRepository).deleteById(1L);
    }

    @Test
    void testGetAllEvenements() {
        when(evenementRepository.findAll()).thenReturn(List.of(evenement));

        List<Evenement> result = evenementService.getAllEvenements();

        assertEquals(1, result.size());
        verify(evenementRepository).findAll();
    }
}