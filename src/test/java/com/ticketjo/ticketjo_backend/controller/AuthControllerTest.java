package com.ticketjo.ticketjo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketjo.ticketjo_backend.dto.LoginDTO;
import com.ticketjo.ticketjo_backend.dto.UtilisateurDTO;
import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.security.JwtUtil;
import com.ticketjo.ticketjo_backend.service.RoleService;
import com.ticketjo.ticketjo_backend.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_ShouldReturnCreatedUser() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom("Doe");
        dto.setPrenom("John");
        dto.setEmail("john@example.com");
        dto.setMotDePasse("Password1"); // Doit respecter la validation
        dto.setAdresse("123 rue Exemple, Paris");
        dto.setTelephone("+33612345678");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("Doe");
        utilisateur.setPrenom("John");
        utilisateur.setEmail("john@example.com");

        Role role = new Role();
        role.setNomRole("USER");
        utilisateur.setRole(role);

        Mockito.when(roleService.trouverRoleParNom("USER")).thenReturn(role);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        Mockito.when(utilisateurService.createUtilisateur(any(Utilisateur.class))).thenReturn(utilisateur);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testLogin_ShouldReturnToken() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("john@example.com");
        loginDTO.setMotDePasse("password");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("john@example.com");
        utilisateur.setMotDePasse("encodedPass");

        Role role = new Role();
        role.setNomRole("USER");
        utilisateur.setRole(role);

        Mockito.when(utilisateurService.getUtilisateurByEmail("john@example.com")).thenReturn(Optional.of(utilisateur));
        Mockito.when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);
        Mockito.when(jwtUtil.generateToken("john@example.com")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testLogin_ShouldReturn404_WhenUserNotFound() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("inconnu@example.com");
        loginDTO.setMotDePasse("password");

        Mockito.when(utilisateurService.getUtilisateurByEmail("inconnu@example.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testLogin_ShouldReturn401_WhenPasswordIncorrect() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("john@example.com");
        loginDTO.setMotDePasse("wrongPassword");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("john@example.com");
        utilisateur.setMotDePasse("encodedPass");

        Mockito.when(utilisateurService.getUtilisateurByEmail("john@example.com"))
                .thenReturn(Optional.of(utilisateur));
        Mockito.when(passwordEncoder.matches("wrongPassword", "encodedPass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}

