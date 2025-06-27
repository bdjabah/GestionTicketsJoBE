package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.TicketCommandeDTO;
import com.ticketjo.ticketjo_backend.mapper.TicketCommandeMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.model.TicketCommande;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.repository.TicketCatalogueRepository;
import com.ticketjo.ticketjo_backend.service.TicketCommandeService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ticket-commandes")
public class TicketCommandeController {

    private final TicketCommandeService service;
    private final TicketCommandeMapper mapper;
    private final CommandeRepository commandeRepository;
    private final TicketCatalogueRepository ticketCatalogueRepository;

    public TicketCommandeController(
        TicketCommandeService service,
        TicketCommandeMapper mapper,
        CommandeRepository commandeRepository,
        TicketCatalogueRepository ticketCatalogueRepository
    ) {
        this.service = service;
        this.mapper = mapper;
        this.commandeRepository = commandeRepository;
        this.ticketCatalogueRepository = ticketCatalogueRepository;
    }
    
    /**
     * Enregistre une nouvelle ligne de commande.
     */
    @PostMapping
    public ResponseEntity<TicketCommandeDTO> create(@RequestBody @Valid TicketCommandeDTO dto) {
        
        // Étape 1 : Vérifie que la commande existe
        Commande commande = commandeRepository.findById(dto.getCommandeId())
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        // Étape 2 : Vérifie que le ticket du catalogue existe
        TicketCatalogue ticketCatalogue = ticketCatalogueRepository.findById(dto.getTicketCatalogueId())
                .orElseThrow(() -> new RuntimeException("TicketCatalogue introuvable"));

        // Étape 3 : Conversion du DTO vers l'entité à l’aide du mapper
        // Cela permet de passer d'un objet de transfert à une vraie entité JPA persistable
        TicketCommande entity = mapper.toEntity(dto, commande, ticketCatalogue);

        // Étape 4 : Enregistrement en base via le service
        // On passe directement l'entité, déjà enrichie avec les relations
        TicketCommande saved = service.save(entity);

        // Étape 5 : Conversion de l'entité persistée vers un DTO de réponse
        // Ceci permet de renvoyer une réponse claire, sans exposer toute l'entité
        TicketCommandeDTO responseDTO = mapper.toDTO(saved);

        // Étape 6 : Retourne une réponse HTTP 200 OK avec l'objet DTO du ticket créé
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Liste toutes les lignes de commande pour une commande donnée.
     */
    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<TicketCommandeDTO>> getByCommande(@PathVariable Long commandeId) {
        List<TicketCommande> entities = service.findByCommande(commandeId);
        List<TicketCommandeDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}