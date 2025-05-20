package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.RoleDTO;
import com.ticketjo.ticketjo_backend.dto.UtilisateurDTO;
import com.ticketjo.ticketjo_backend.mapper.UtilisateurMapper;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.security.JwtUtil;
import com.ticketjo.ticketjo_backend.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurControllerTest {

    private final UtilisateurService utilisateurService = mock(UtilisateurService.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class); // Mock du JwtUtil
    private final UtilisateurController controller = new UtilisateurController(utilisateurService, jwtUtil);

    @Test
    void testCreateUtilisateur() {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Jean");
        dto.setPrenom("Dupont");
        dto.setEmail("jean@example.com");
        dto.setMotDePasse("Password1!");
        dto.setAdresse("1 rue du code");
        dto.setTelephone("+33612345678");
        dto.setDateInscription(LocalDate.now());

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setIdRole(2L);
        dto.setRole(roleDTO);

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        Utilisateur savedUtilisateur = new Utilisateur();
        savedUtilisateur.setIdUtilisateur(1L);
        savedUtilisateur.setNom(dto.getNom());
        savedUtilisateur.setPrenom(dto.getPrenom());
        savedUtilisateur.setEmail(dto.getEmail());

        when(utilisateurService.createUtilisateur(any())).thenReturn(savedUtilisateur);

        ResponseEntity<UtilisateurDTO> response = controller.createUtilisateur(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Jean", response.getBody().getNom());
    }

    @Test
    void testDeleteUtilisateur() {
        doNothing().when(utilisateurService).deleteUtilisateur(1L);
        ResponseEntity<Void> response = controller.deleteUtilisateur(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetUtilisateurByEmail_Found() {
        String email = "jean@example.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setNom("Jean");

        when(utilisateurService.getUtilisateurByEmail(email)).thenReturn(Optional.of(utilisateur));

        ResponseEntity<UtilisateurDTO> response = controller.getUtilisateurByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jean", response.getBody().getNom());
    }

    @Test
    void testGetUtilisateurByEmail_NotFound() {
        when(utilisateurService.getUtilisateurByEmail("absent@example.com")).thenReturn(Optional.empty());

        ResponseEntity<UtilisateurDTO> response = controller.getUtilisateurByEmail("absent@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllUtilisateurs() {
        Utilisateur u1 = new Utilisateur();
        u1.setNom("Jean");
        u1.setEmail("jean@example.com");

        Utilisateur u2 = new Utilisateur();
        u2.setNom("Marie");
        u2.setEmail("marie@example.com");

        when(utilisateurService.getAllUtilisateurs()).thenReturn(List.of(u1, u2));

        ResponseEntity<List<UtilisateurDTO>> response = controller.getAllUtilisateurs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetCurrentUser() {
        String token = "Bearer faketoken";
        String fakeEmail = "jean@example.com";

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("Jean");
        utilisateur.setEmail(fakeEmail);

        when(jwtUtil.extractEmail("faketoken")).thenReturn(fakeEmail);
        when(utilisateurService.getUtilisateurByEmail(fakeEmail)).thenReturn(Optional.of(utilisateur));

        ResponseEntity<UtilisateurDTO> response = controller.getCurrentUser(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jean", response.getBody().getNom());
    }
}