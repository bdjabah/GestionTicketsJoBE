package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.TicketVenduDTO;
import com.ticketjo.ticketjo_backend.mapper.TicketVenduMapper;
import com.ticketjo.ticketjo_backend.model.TicketVendu;
import com.ticketjo.ticketjo_backend.model.enums.StatutTicket;
import com.ticketjo.ticketjo_backend.service.TicketVenduService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour gérer les tickets vendus.
 */
@RestController
@RequestMapping("/api/tickets-vendus")
public class TicketVenduController {

    private final TicketVenduService ticketVenduService;

    public TicketVenduController(TicketVenduService ticketVenduService) {
        this.ticketVenduService = ticketVenduService;
    }

    /** Crée un nouveau ticket vendu. */
    @PostMapping
    public ResponseEntity<TicketVenduDTO> creerTicketVendu(@Valid @RequestBody TicketVenduDTO dto) {
        TicketVendu created = ticketVenduService.creerTicketVendu(TicketVenduMapper.toEntity(dto));
        return ResponseEntity.ok(TicketVenduMapper.toDTO(created));
    }

    /** Récupère tous les tickets vendus. */
    @GetMapping
    public ResponseEntity<List<TicketVenduDTO>> obtenirTousLesTicketsVendus() {
        List<TicketVenduDTO> allDtos = ticketVenduService.obtenirTousLesTicketsVendus().stream()
            .map(TicketVenduMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(allDtos);
    }

    /** Récupère un ticket vendu par ID. */
    @GetMapping("/{id}")
    public ResponseEntity<TicketVenduDTO> getTicketVenduParId(@PathVariable Long id) {
        return ticketVenduService.getTicketVenduParId(id)
            .map(tv -> ResponseEntity.ok(TicketVenduMapper.toDTO(tv)))
            .orElse(ResponseEntity.notFound().build());
    }

    /** Recherche un ticket vendu par sa clé unique. */
    @GetMapping("/cle/{cle}")
    public ResponseEntity<TicketVenduDTO> trouverParCleTicket(@PathVariable String cle) {
        TicketVendu result = ticketVenduService.trouverParCleTicket(cle);
        return result != null
            ? ResponseEntity.ok(TicketVenduMapper.toDTO(result))
            : ResponseEntity.notFound().build();
    }

    /** Récupère tous les tickets vendus pour un utilisateur. */
    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<List<TicketVenduDTO>> obtenirParIdUtilisateur(@PathVariable Long idUtilisateur) {
        List<TicketVenduDTO> dtos = ticketVenduService.obtenirParIdUtilisateur(idUtilisateur).stream()
            .map(TicketVenduMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Récupère tous les tickets vendus liés à une commande. */
    @GetMapping("/commande/{idCommande}")
    public ResponseEntity<List<TicketVenduDTO>> obtenirParIdCommande(@PathVariable Long idCommande) {
        List<TicketVenduDTO> dtos = ticketVenduService.obtenirParIdCommande(idCommande).stream()
            .map(TicketVenduMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Récupère tous les tickets vendus par statut. */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<TicketVenduDTO>> obtenirParStatut(@PathVariable StatutTicket statut) {
        List<TicketVenduDTO> dtos = ticketVenduService.obtenirParStatut(statut).stream()
            .map(TicketVenduMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Met à jour un ticket vendu existant. */
    @PutMapping("/{id}")
    public ResponseEntity<TicketVenduDTO> modifierTicketVendu(@PathVariable Long id, @Valid @RequestBody TicketVenduDTO dto) {
        dto.setIdTicketVendu(id);
        TicketVendu updated = ticketVenduService.creerTicketVendu(TicketVenduMapper.toEntity(dto));
        return ResponseEntity.ok(TicketVenduMapper.toDTO(updated));
    }

    /** Supprime un ticket vendu par ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTicketVendu(@PathVariable Long id) {
        ticketVenduService.supprimerTicketVendu(id);
        return ResponseEntity.noContent().build();
    }
    
}