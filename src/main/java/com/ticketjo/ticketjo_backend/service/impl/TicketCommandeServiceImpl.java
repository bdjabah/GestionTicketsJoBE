package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.model.TicketCommande;
import com.ticketjo.ticketjo_backend.repository.TicketCommandeRepository;
import com.ticketjo.ticketjo_backend.service.TicketCommandeService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation concrète de TicketCommandeService.
 * Fournit la logique métier pour gérer les lignes de commande (TicketCommande).
 */
@Service
public class TicketCommandeServiceImpl implements TicketCommandeService {

    private final TicketCommandeRepository ticketCommandeRepository;

    public TicketCommandeServiceImpl(TicketCommandeRepository ticketCommandeRepository) {
        this.ticketCommandeRepository = ticketCommandeRepository;
    }

    /**
     * Persiste une ligne de commande (TicketCommande) dans la base de données.
     *
     * @param ticketCommande L'entité à sauvegarder.
     * @return L'entité persistée.
     */
    @Override
    public TicketCommande save(TicketCommande ticketCommande) {
        return ticketCommandeRepository.save(ticketCommande);
    }

    /**
     * Récupère toutes les lignes de commande liées à une commande donnée.
     *
     * @param idCommande L'identifiant de la commande.
     * @return Liste des TicketCommande associés.
     */
    @Override
    public List<TicketCommande> findByCommande(Long idCommande) {
        return ticketCommandeRepository.findByCommande_IdCommande(idCommande);
    }
}