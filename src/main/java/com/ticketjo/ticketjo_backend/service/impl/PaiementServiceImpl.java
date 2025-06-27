package com.ticketjo.ticketjo_backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.repository.PaiementRepository;
import com.ticketjo.ticketjo_backend.service.PaiementService;

@Service
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;

    public PaiementServiceImpl(PaiementRepository paiementRepository, CommandeRepository commandeRepository) {
        this.paiementRepository = paiementRepository;
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Paiement creerPaiement(Paiement paiement) {
        if (paiement.getCommande() == null || paiement.getCommande().getIdCommande() == null) {
            throw new RuntimeException("La commande associée au paiement est manquante");
        }

        Commande commande = commandeRepository.findById(paiement.getCommande().getIdCommande())
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        paiement.setCommande(commande);

        if (commande.getUtilisateur() != null) {
            paiement.setUtilisateur(commande.getUtilisateur());
        } else {
            throw new RuntimeException("Utilisateur introuvable pour la commande");
        }

        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement trouverParCommande(Long idCommande) {
        Commande commande = commandeRepository.findById(idCommande).orElse(null);
        if (commande != null) {
            return paiementRepository.findByCommande(commande);
        }
        return null;
    }

    @Override
    public List<Paiement> listerPaiementsParStatut(StatutPaiement statut) {
        return paiementRepository.findByStatut(statut);
    }

    @Override
    public List<Paiement> listerPaiementsUtilisateur(Long idUtilisateur) {
        return paiementRepository.findByCommande_Utilisateur_IdUtilisateur(idUtilisateur);
    }

    @Override
    public void marquerPaiementValide(String paymentIntentId) {
        Paiement paiement = paiementRepository.findByPaymentIntentId(paymentIntentId);
        if (paiement != null) {
            if (paiement.getCommande() == null && paiement.getIdPaiement() != null) {
                paiement = paiementRepository.findById(paiement.getIdPaiement())
                        .orElse(paiement);
            }
            paiement.setStatut(StatutPaiement.VALIDE);
            paiementRepository.save(paiement);
        }
    }

    @Override
    public void marquerPaiementEchoue(String paymentIntentId) {
        Paiement paiement = paiementRepository.findByPaymentIntentId(paymentIntentId);
        if (paiement != null) {
            if (paiement.getCommande() == null && paiement.getIdPaiement() != null) {
                paiement = paiementRepository.findById(paiement.getIdPaiement())
                        .orElse(paiement);
            }
            paiement.setStatut(StatutPaiement.ECHOUE);
            paiementRepository.save(paiement);
        }
    }

    @Override
    public Paiement trouverParIntentId(String intentId) {
        return paiementRepository.findByPaymentIntentId(intentId);
    }

    @Override
    public Paiement mettreAJourPaiement(Paiement paiement) {
        Paiement existingPaiement = paiementRepository.findById(paiement.getIdPaiement())
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        existingPaiement.setMontantPaiement(paiement.getMontantPaiement());
        existingPaiement.setStatut(paiement.getStatut());
        existingPaiement.setDatePaiement(paiement.getDatePaiement());
        existingPaiement.setMethodePaiement(paiement.getMethodePaiement());

        return paiementRepository.save(existingPaiement);
    }
}