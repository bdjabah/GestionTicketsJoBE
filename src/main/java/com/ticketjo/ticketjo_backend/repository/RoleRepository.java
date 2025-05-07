package com.ticketjo.ticketjo_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketjo.ticketjo_backend.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByNomRole(String nomRole);// Pour vérifier ou attribuer un rôle à un utilisateur (genre ADMIN,
													// USER,
}