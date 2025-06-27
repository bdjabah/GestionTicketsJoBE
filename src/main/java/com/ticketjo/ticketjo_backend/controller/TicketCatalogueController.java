package com.ticketjo.ticketjo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketjo.ticketjo_backend.dto.TicketCatalogueDTO;
import com.ticketjo.ticketjo_backend.mapper.TicketCatalogueMapper;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.service.TicketCatalogueService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketCatalogueController {

    private final TicketCatalogueService ticketCatalogueService;
    private final ObjectMapper objectMapper;

    /**
     * Crée un nouveau ticket (offre catalogue).
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketCatalogueDTO> createTicket(
            @RequestPart("ticket") String ticketJson,
            @RequestPart("image") MultipartFile image) { // image devient obligatoire
        try {
            // Vérifie que l'image n'est pas vide
            if (image == null || image.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(null); // Ou un message personnalisé avec une classe ErrorDTO
            }

            TicketCatalogueDTO dto = objectMapper.readValue(ticketJson, TicketCatalogueDTO.class);
            TicketCatalogue ticketCatalogue = TicketCatalogueMapper.toEntity(dto);

            String filename = image.getOriginalFilename();
            Path uploadDir = Paths.get("uploads");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            ticketCatalogue.setImageTicket(filename);

            TicketCatalogue created = ticketCatalogueService.creerTicket(ticketCatalogue);
            return new ResponseEntity<>(TicketCatalogueMapper.toDTO(created), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour une offre de ticket existante.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketCatalogueDTO> updateTicket(
            @PathVariable Long id,
            @RequestPart("ticket") String ticketJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            TicketCatalogueDTO dto = objectMapper.readValue(ticketJson, TicketCatalogueDTO.class);
            dto.setIdTicket(id);
            TicketCatalogue ticketCatalogue = TicketCatalogueMapper.toEntity(dto);

            if (image != null && !image.isEmpty()) {
                String filename = image.getOriginalFilename();
                Path uploadDir = Paths.get("uploads");

                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path filePath = uploadDir.resolve(filename);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                ticketCatalogue.setImageTicket(filename);
            } else {
                TicketCatalogue original = ticketCatalogueService.getTicketById(id).orElse(null);
                if (original != null) {
                    ticketCatalogue.setImageTicket(original.getImageTicket());
                }
            }

            TicketCatalogue updated = ticketCatalogueService.mettreAJourTicket(ticketCatalogue);
            return new ResponseEntity<>(TicketCatalogueMapper.toDTO(updated), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère tous les tickets (catalogue complet).
     */
    @GetMapping
    public ResponseEntity<List<TicketCatalogueDTO>> getAllTickets() {
        List<TicketCatalogueDTO> dtos = ticketCatalogueService.obtenirTousLesTickets()
                .stream().map(TicketCatalogueMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Récupère un ticket spécifique par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketCatalogueDTO> getTicketById(@PathVariable Long id) {
        Optional<TicketCatalogue> ticketCatalogue = ticketCatalogueService.getTicketById(id);
        return ticketCatalogue.map(value -> new ResponseEntity<>(TicketCatalogueMapper.toDTO(value), HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Supprime un ticket du catalogue.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketCatalogueService.supprimerTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Récupère les tickets disponibles (stock > 0).
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<TicketCatalogueDTO>> getAvailableTickets() {
        List<TicketCatalogueDTO> dtos = ticketCatalogueService.obtenirTicketsDisponibles()
                .stream().map(TicketCatalogueMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Recherche des tickets par type (ex: solo, duo, famille...).
     */
    @GetMapping("/recherche")
    public ResponseEntity<List<TicketCatalogueDTO>> searchByType(@RequestParam String type) {
        List<TicketCatalogueDTO> dtos = ticketCatalogueService.rechercherParTypeTicket(type)
                .stream().map(TicketCatalogueMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}



