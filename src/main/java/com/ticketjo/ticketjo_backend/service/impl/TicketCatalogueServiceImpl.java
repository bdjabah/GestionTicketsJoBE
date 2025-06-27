package com.ticketjo.ticketjo_backend.service.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.repository.TicketCatalogueRepository;
import com.ticketjo.ticketjo_backend.service.TicketCatalogueService;

@Service
public class TicketCatalogueServiceImpl implements TicketCatalogueService {

    private final TicketCatalogueRepository ticketCatalogueRepository;

    public TicketCatalogueServiceImpl(TicketCatalogueRepository ticketCatalogueRepository) {
        this.ticketCatalogueRepository = ticketCatalogueRepository;
    }

    @Override
    public TicketCatalogue creerTicket(TicketCatalogue ticketCatalogue) {
        return ticketCatalogueRepository.save(ticketCatalogue);
    }

    @Override
    public TicketCatalogue mettreAJourTicket(TicketCatalogue ticketCatalogue) {
        Optional<TicketCatalogue> existingTicket = ticketCatalogueRepository.findById(ticketCatalogue.getIdTicket());
        if (existingTicket.isPresent()) {
            return ticketCatalogueRepository.save(ticketCatalogue);
        } else {
            throw new IllegalArgumentException("Ticket non trouvé avec l'ID : " + ticketCatalogue.getIdTicket());
        }
    }

    @Override
    public void supprimerTicket(Long idTicket) {
        ticketCatalogueRepository.deleteById(idTicket);
    }

    @Override
    public List<TicketCatalogue> obtenirTousLesTickets() {
        return ticketCatalogueRepository.findAll();
    }

    @Override
    public Optional<TicketCatalogue> getTicketById(Long id) {
        return ticketCatalogueRepository.findById(id);
    }

    @Override
    public List<TicketCatalogue> obtenirTicketsDisponibles() {
        return ticketCatalogueRepository.findByStockGreaterThan(0);
    }

    @Override
    public List<TicketCatalogue> rechercherParTypeTicket(String type) {
        return ticketCatalogueRepository.findByTypeTicketContainingIgnoreCase(type);
    }
}