package com.ticketjo.ticketjo_backend.service.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.model.Ticket;
import com.ticketjo.ticketjo_backend.repository.TicketRepository;
import com.ticketjo.ticketjo_backend.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket creerTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> obtenirTicketsParCommande(Commande commande) {
        return ticketRepository.findByCommande(commande);
    }

    @Override
    public List<Ticket> obtenirTicketsParUtilisateur(Long idUtilisateur) {
        return ticketRepository.findByCommande_Utilisateur_IdUtilisateur(idUtilisateur);
    }

    @Override
    public List<Ticket> obtenirTicketsParEvenement(Evenement evenement) {
        return ticketRepository.findByEvenement(evenement);
    }

    @Override
    public Ticket trouverTicketParCle(String cleTicket) {
        return ticketRepository.findByCleTicket(cleTicket);
    }

    @Override
    public void supprimerTicket(Long idTicket) {
        ticketRepository.deleteById(idTicket);
    }

    @Override
    public Ticket mettreAJourTicket(Ticket ticket) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getIdTicket());
        if (existingTicket.isPresent()) {
            return ticketRepository.save(ticket); // Save = update si ID existe
        } else {
            throw new IllegalArgumentException("Ticket non trouvé avec l'ID : " + ticket.getIdTicket());
        }
    }

    @Override
    public List<Ticket> obtenirTousLesTickets() {
        return ticketRepository.findAll();
    }
    
    @Override
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
}