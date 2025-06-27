package com.ticketjo.ticketjo_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.model.TicketVendu;
import com.ticketjo.ticketjo_backend.model.enums.StatutTicket;
import com.ticketjo.ticketjo_backend.repository.TicketCatalogueRepository;
import com.ticketjo.ticketjo_backend.repository.TicketVenduRepository;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.service.TicketVenduService;

@Service
public class TicketVenduServiceImpl implements TicketVenduService {

    private final TicketVenduRepository repository;
    private final TicketCatalogueRepository ticketCatalogueRepository;
    private final CommandeRepository commandeRepository;

    public TicketVenduServiceImpl(TicketVenduRepository repository, TicketCatalogueRepository ticketCatalogueRepository,CommandeRepository commandeRepository) {
        this.repository = repository;
        this.ticketCatalogueRepository = ticketCatalogueRepository;
        this.commandeRepository = commandeRepository;
    }

    @Override
    public TicketVendu creerTicketVendu(TicketVendu ticketVendu) {
        return repository.save(ticketVendu);
    }

    @Override
    public List<TicketVendu> obtenirTousLesTicketsVendus() {
        return repository.findAll();
    }

    @Override
    public Optional<TicketVendu> getTicketVenduParId(Long id) {
        return repository.findById(id);
    }

    @Override
    public TicketVendu trouverParCleTicket(String cle) {
        return repository.findByCleTicket(cle);
    }

    @Override
    public List<TicketVendu> obtenirParIdUtilisateur(Long idUtilisateur) {
        return repository.findByUtilisateur_IdUtilisateur(idUtilisateur);
    }

    @Override
    public List<TicketVendu> obtenirParIdCommande(Long idCommande) {
        return repository.findByCommande_IdCommande(idCommande);
    }

    @Override
    public List<TicketVendu> obtenirParStatut(StatutTicket statutTicket) {
        return repository.findByStatutTicket(statutTicket);
    }

    @Override
    public void supprimerTicketVendu(Long id) {
        repository.deleteById(id);
    }

    /**
     * Génère les tickets vendus pour chaque ligne de commande, avec décrémentation du stock.
     */
    
    @Override
    public void genererTicketsPourCommande(Commande commande) {
        if (commande == null || commande.getIdCommande() == null) {
            throw new RuntimeException("Commande invalide ou id manquant.");
        }

        // On recharge la commande avec les ticketsCommandes bien initialisés
        Commande commandeComplete = commandeRepository.findByIdWithTickets(commande.getIdCommande())
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec ses détails."));

        if (commandeComplete.getTicketsCommandes() == null || commandeComplete.getTicketsCommandes().isEmpty()) {
            throw new RuntimeException("Aucun ticket commandé pour cette commande.");
        }

        String cleUtilisateur = commandeComplete.getUtilisateur().getCleUtilisateur();

        if (cleUtilisateur == null || cleUtilisateur.isEmpty()) {
            throw new RuntimeException("Clé utilisateur manquante, impossible de générer le QRCode sécurisé.");
        }

        commandeComplete.getTicketsCommandes().forEach(ticketCommande -> {
            TicketCatalogue ticketCatalogue = ticketCommande.getTicketCatalogue();

            if (ticketCatalogue == null) {
                throw new RuntimeException("TicketCatalogue introuvable pour un des tickets commandés");
            }

            if (ticketCatalogue.getStock() < ticketCommande.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour le ticket : " + ticketCatalogue.getTypeTicket());
            }

            for (int i = 0; i < ticketCommande.getQuantite(); i++) {
                TicketVendu ticketVendu = new TicketVendu();

                String cleTicket = UUID.randomUUID().toString();
                ticketVendu.setCleTicket(cleTicket);
                String qrCodeSecurise = cleUtilisateur + "-" + cleTicket;
                ticketVendu.setQrCode(qrCodeSecurise);

                ticketVendu.setStatutTicket(StatutTicket.NON_UTILISE);
                ticketVendu.setDateAchat(LocalDateTime.now());
                ticketVendu.setCommande(commandeComplete);
                ticketVendu.setUtilisateur(commandeComplete.getUtilisateur());
                ticketVendu.setTicketCatalogue(ticketCatalogue);
                ticketVendu.setTypeTicket(ticketCatalogue.getTypeTicket());
                ticketVendu.setPrixTicket(ticketCatalogue.getPrixTicket());
                ticketVendu.setImageTicket(ticketCatalogue.getImageTicket());
                ticketVendu.setNom(ticketCommande.getNom());
                ticketVendu.setPrenom(ticketCommande.getPrenom());

                repository.save(ticketVendu);
            }

            ticketCatalogue.setStock(ticketCatalogue.getStock() - ticketCommande.getQuantite());
            ticketCatalogueRepository.save(ticketCatalogue);
        });
    
    }
}
