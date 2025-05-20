package com.ticketjo.ticketjo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketjo.ticketjo_backend.dto.TicketDTO;
import com.ticketjo.ticketjo_backend.mapper.TicketMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.service.TicketService;

import io.jsonwebtoken.io.IOException;
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
public class TicketController {

    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    /**
     * Crée un nouveau ticket (avec JSON + image).
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketDTO> createTicket(
            @RequestPart("ticket") String ticketJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            TicketDTO dto = objectMapper.readValue(ticketJson, TicketDTO.class);
            Ticket ticket = TicketMapper.toEntity(dto);
            ticket.setStatutTicket("DISPONIBLE");

            if (image != null && !image.isEmpty()) {
                ticket.setImageTicket(image.getOriginalFilename());
                try {
                    String filename = image.getOriginalFilename();
                    Path uploadDir = Paths.get("uploads");

                    if (!Files.exists(uploadDir)) {
                        Files.createDirectories(uploadDir);
                    }

                    Path filePath = uploadDir.resolve(filename);
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    ticket.setImageTicket(filename);
                } catch (IOException e) {
                    throw new RuntimeException("Échec de l'enregistrement du fichier image", e);
                }
            }

            Ticket created = ticketService.creerTicket(ticket);
            return new ResponseEntity<>(TicketMapper.toDTO(created), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un ticket existant (avec JSON + image).
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketDTO> updateTicket(
            @PathVariable Long id,
            @RequestPart("ticket") String ticketJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            TicketDTO dto = objectMapper.readValue(ticketJson, TicketDTO.class);
            dto.setIdTicket(id);
            Ticket ticket = TicketMapper.toEntity(dto);
            ticket.setStatutTicket("MIS_A_JOUR");

            if (image != null && !image.isEmpty()) {
                try {
                    String filename = image.getOriginalFilename();
                    Path uploadDir = Paths.get("uploads");

                    if (!Files.exists(uploadDir)) {
                        Files.createDirectories(uploadDir);
                    }

                    Path filePath = uploadDir.resolve(filename);
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    ticket.setImageTicket(filename);
                } catch (IOException e) {
                    throw new RuntimeException("Échec de l'enregistrement du fichier image", e);
                }
            } else {
                // ⚠️ Si aucune nouvelle image, on garde l’ancienne
                Ticket original = ticketService.getTicketById(id).orElse(null);
                if (original != null) {
                    ticket.setImageTicket(original.getImageTicket());
                }
            }

            Ticket updated = ticketService.mettreAJourTicket(ticket);
            return new ResponseEntity<>(TicketMapper.toDTO(updated), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        return ticket.map(value -> new ResponseEntity<>(TicketMapper.toDTO(value), HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * Supprime un ticket.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.supprimerTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Tickets liés à une commande.
     */
    @GetMapping("/commande/{idCommande}")
    public ResponseEntity<List<TicketDTO>> getTicketsByCommande(@PathVariable Long idCommande) {
        Commande commande = new Commande();
        commande.setIdCommande(idCommande);
        List<TicketDTO> dtos = ticketService.obtenirTicketsParCommande(commande)
                .stream().map(TicketMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Tickets liés à un utilisateur.
     */
    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUtilisateur(@PathVariable Long idUtilisateur) {
        List<TicketDTO> dtos = ticketService.obtenirTicketsParUtilisateur(idUtilisateur)
                .stream().map(TicketMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Tickets liés à un événement.
     */
    @GetMapping("/evenement/{idEvenement}")
    public ResponseEntity<List<TicketDTO>> getTicketsByEvenement(@PathVariable Long idEvenement) {
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(idEvenement);
        List<TicketDTO> dtos = ticketService.obtenirTicketsParEvenement(evenement)
                .stream().map(TicketMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Recherche d’un ticket par sa clé unique.
     */
    @GetMapping("/cle/{cleTicket}")
    public ResponseEntity<TicketDTO> getTicketByCle(@PathVariable String cleTicket) {
        Ticket ticket = ticketService.trouverTicketParCle(cleTicket);
        if (ticket != null) {
            return new ResponseEntity<>(TicketMapper.toDTO(ticket), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Liste tous les tickets.
     */
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> dtos = ticketService.obtenirTousLesTickets()
                .stream().map(TicketMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
