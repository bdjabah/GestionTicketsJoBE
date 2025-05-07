package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setNomRole("ADMIN");
    }

    @Test
    void testCreerRole_WhenRoleDoesNotExist_ShouldSaveRole() {
        when(roleRepository.findByNomRole("ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.creerRole(role);

        assertEquals("ADMIN", result.getNomRole());
        verify(roleRepository).save(role);
    }

    @Test
    void testCreerRole_WhenRoleAlreadyExists_ShouldThrowException() {
        when(roleRepository.findByNomRole("ADMIN")).thenReturn(Optional.of(role));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.creerRole(role)
        );

        assertEquals("Ce rôle existe déjà", exception.getMessage());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void testTrouverRoleParNom_WhenRoleExists_ShouldReturnRole() {
        when(roleRepository.findByNomRole("ADMIN")).thenReturn(Optional.of(role));

        Role result = roleService.trouverRoleParNom("ADMIN");

        assertEquals("ADMIN", result.getNomRole());
        verify(roleRepository).findByNomRole("ADMIN");
    }

    @Test
    void testTrouverRoleParNom_WhenRoleDoesNotExist_ShouldThrowException() {
        when(roleRepository.findByNomRole("ADMIN")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.trouverRoleParNom("ADMIN")
        );

        assertEquals("Role introuvable : ADMIN", exception.getMessage());
    }
}
