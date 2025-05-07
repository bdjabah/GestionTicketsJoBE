package com.ticketjo.ticketjo_backend.service.impl;


import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setNom("Doe");
        utilisateur.setPrenom("John");
        utilisateur.setEmail("john.doe@example.com");
    }

    @Test
    void testCreateUtilisateur_WhenEmailIsUnique_ShouldCreateUserWithUUIDAndDate() {
        // Simuler que l'email n'existe pas déjà
        when(utilisateurRepository.existsByEmail(utilisateur.getEmail())).thenReturn(false);
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appel de la méthode
        Utilisateur savedUser = utilisateurService.createUtilisateur(utilisateur);

        // Vérifications
        assertNotNull(savedUser.getCleUtilisateur());
        assertNotNull(savedUser.getDateInscription());
        assertEquals(LocalDate.now(), savedUser.getDateInscription());
        verify(utilisateurRepository).save(savedUser);
    }

    @Test
    void testCreateUtilisateur_WhenEmailExists_ShouldThrowException() {
        // Simuler que l'email existe déjà
        when(utilisateurRepository.existsByEmail(utilisateur.getEmail())).thenReturn(true);

        // Vérification de l'exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> utilisateurService.createUtilisateur(utilisateur)
        );

        assertEquals("Un compte avec cet email existe déjà.", exception.getMessage());
        verify(utilisateurRepository, never()).save(any());
    }
}