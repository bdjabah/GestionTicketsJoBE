package com.ticketjo.ticketjo_backend.controller;


import com.ticketjo.ticketjo_backend.dto.PanierDTO;
import com.ticketjo.ticketjo_backend.mapper.PanierMapper;
import com.ticketjo.ticketjo_backend.model.Panier;
import com.ticketjo.ticketjo_backend.service.PanierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des paniers.
 */
@RestController
@RequestMapping("/api/paniers")
@RequiredArgsConstructor
public class PanierController {

    private final PanierService panierService;

    /**
     * Crée un nouveau panier à partir d’un DTO validé.
     * @param panierDTO Détails du panier à ajouter.
     * @return Le panier créé sous forme de DTO avec un statut 201.
     */
    @PostMapping
    public ResponseEntity<PanierDTO> ajouterPanier(@RequestBody @Valid PanierDTO panierDTO) {
        Panier panier = PanierMapper.toEntity(panierDTO);
        Panier createdPanier = panierService.ajouterPanier(panier);
        return new ResponseEntity<>(PanierMapper.toDTO(createdPanier), HttpStatus.CREATED);
    }

    /**
     * Supprime un panier existant par son identifiant.
     * @param idPanier ID du panier à supprimer.
     * @return Réponse vide avec statut 204 si suppression réussie.
     */
    @DeleteMapping("/{idPanier}")
    public ResponseEntity<Void> supprimerPanier(@PathVariable Long idPanier) {
        panierService.supprimerPanier(idPanier);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Récupère tous les paniers associés à un utilisateur donné.
     * @param idUtilisateur ID de l'utilisateur concerné.
     * @return Liste des paniers liés à cet utilisateur sous forme de DTO.
     */
    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<List<PanierDTO>> listerPaniersParUtilisateur(@PathVariable Long idUtilisateur) {
        List<PanierDTO> paniers = panierService.listerPaniersParUtilisateur(idUtilisateur)
                .stream()
                .map(PanierMapper::toDTO)
                .toList();
        return new ResponseEntity<>(paniers, HttpStatus.OK);
    }
}