package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.Role;
import com.ticketjo.ticketjo_backend.repository.RoleRepository;
import com.ticketjo.ticketjo_backend.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role creerRole(Role role) {
        // Sauvegarde un nouveau rôle en base
    	if (roleRepository.findByNomRole(role.getNomRole()).isPresent()) {
    	    throw new IllegalArgumentException("Ce rôle existe déjà");
    	}
        return roleRepository.save(role);
        
    }

    @Override
    public Role trouverRoleParNom(String nomRole) {
        // Recherche d'un rôle par son nom
        return roleRepository.findByNomRole(nomRole)
        		.orElseThrow(() -> new IllegalArgumentException("Role introuvable : " + nomRole));
    }
}