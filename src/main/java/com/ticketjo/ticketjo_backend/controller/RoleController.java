package com.ticketjo.ticketjo_backend.controller;


import com.ticketjo.ticketjo_backend.dto.RoleDTO;
import com.ticketjo.ticketjo_backend.mapper.RoleMapper;
import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer les opérations liées aux rôles.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Endpoint pour créer un nouveau rôle.
     * @param role L'objet rôle à créer.
     * @return Le rôle créé avec un statut 201 (Created).
     */
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        Role role = RoleMapper.toEntity(roleDTO);
        Role created = roleService.creerRole(role);
        return new ResponseEntity<>(RoleMapper.toDTO(created), HttpStatus.CREATED);
    }

    /**
     * Endpoint pour récupérer un rôle par son nom.
     * @param nomRole Le nom du rôle recherché.
     * @return Le rôle correspondant ou 404 si non trouvé.
     */
    @GetMapping("/{nomRole}")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String nomRole) {
        Role role = roleService.trouverRoleParNom(nomRole);
        if (role != null) {
            return new ResponseEntity<>(RoleMapper.toDTO(role), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
