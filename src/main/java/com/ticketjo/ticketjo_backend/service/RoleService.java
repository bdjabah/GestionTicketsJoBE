package com.ticketjo.ticketjo_backend.service;


import com.ticketjo.ticketjo_backend.model.Role;

public interface RoleService {

    /**
     * Crée un nouveau rôle dans le système.
     * @param role L'objet Role à créer.
     * @return Le rôle créé.
     */
    Role creerRole(Role role);

    /**
     * Cherche un rôle par son nom.
     * @param nomRole Le nom du rôle.
     * @return Le rôle trouvé ou null si aucun rôle trouvé.
     */
    Role trouverRoleParNom(String nomRole);
}
