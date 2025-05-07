package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.RoleDTO;
import com.ticketjo.ticketjo_backend.model.Role;

/**
 * Classe utilitaire pour convertir entre Role (entité) et RoleDTO.
 * Oui, parce que visiblement on doit tout faire nous-mêmes.
 */
public class RoleMapper {

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe.
     * C’est une classe utilitaire, pas une auberge.
     */
    private RoleMapper() {
        throw new UnsupportedOperationException("Classe utilitaire - instanciation interdite");
    }

    /**
     * Convertit une entité Role en DTO.
     * On prend toutes les infos utiles et on les met dans un objet que le frontend peut digérer.
     *
     * @param role L'entité Role (venant de la base de données)
     * @return Le DTO (pour exposer au monde sans honte)
     */
    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;

        RoleDTO dto = new RoleDTO();
        dto.setIdRole(role.getIdRole());
        dto.setNomRole(role.getNomRole());
        dto.setDescriptionRole(role.getDescriptionRole()); // Ne pas oublier ce champ sinon on a une coquille vide
        return dto;
    }

    /**
     * Convertit un DTO Role en entité.
     * Parce qu'à un moment faut bien retourner en base de données, hein.
     *
     * @param dto Le DTO (souvent venu du frontend, donc suspect)
     * @return L'entité Role correspondante (plus fiable, mais pas toujours)
     */
    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;

        Role role = new Role();
        role.setIdRole(dto.getIdRole());
        role.setNomRole(dto.getNomRole());
        role.setDescriptionRole(dto.getDescriptionRole());
        return role;
    }
}