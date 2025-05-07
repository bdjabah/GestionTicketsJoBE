package com.ticketjo.ticketjo_backend.controller;
import com.ticketjo.ticketjo_backend.dto.RoleDTO;
import com.ticketjo.ticketjo_backend.mapper.RoleMapper;
import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRole_shouldReturnCreatedRole() {
        RoleDTO inputDTO = new RoleDTO();
        inputDTO.setNomRole("ADMIN");
        inputDTO.setDescriptionRole("Peut tout faire");

        Role role = new Role();
        role.setIdRole(1L);
        role.setNomRole("ADMIN");
        role.setDescriptionRole("Peut tout faire");

        when(roleService.creerRole(any(Role.class))).thenReturn(role);

        ResponseEntity<RoleDTO> response = roleController.createRole(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADMIN", response.getBody().getNomRole());
        assertEquals("Peut tout faire", response.getBody().getDescriptionRole());
        verify(roleService, times(1)).creerRole(any(Role.class));
    }

    @Test
    void getRoleByName_shouldReturnRoleIfFound() {
        String roleName = "USER";

        Role role = new Role();
        role.setIdRole(2L);
        role.setNomRole("USER");
        role.setDescriptionRole("Accès limité");

        when(roleService.trouverRoleParNom(roleName)).thenReturn(role);

        ResponseEntity<RoleDTO> response = roleController.getRoleByName(roleName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("USER", response.getBody().getNomRole());
        assertEquals("Accès limité", response.getBody().getDescriptionRole());
        verify(roleService, times(1)).trouverRoleParNom(roleName);
    }

    @Test
    void getRoleByName_shouldReturnNotFoundIfMissing() {
        String roleName = "UNKNOWN";

        when(roleService.trouverRoleParNom(roleName)).thenReturn(null);

        ResponseEntity<RoleDTO> response = roleController.getRoleByName(roleName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(roleService, times(1)).trouverRoleParNom(roleName);
    }
}
