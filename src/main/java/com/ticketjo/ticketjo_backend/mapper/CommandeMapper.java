package com.ticketjo.ticketjo_backend.mapper;
import com.ticketjo.ticketjo_backend.dto.CommandeDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.TicketVendu;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.model.enums.StatutCommande;

import java.util.stream.Collectors;

public class CommandeMapper {

    public static CommandeDTO toDTO(Commande commande) {
        if (commande == null)
            return null;

        CommandeDTO dto = new CommandeDTO();
        dto.setIdCommande(commande.getIdCommande());
        dto.setDateCommande(commande.getDateCommande());
        dto.setStatut(commande.getStatutCommande());
        dto.setTotalCommande(commande.getTotalCommande());

        if (commande.getUtilisateur() != null) {
            dto.setIdUtilisateur(commande.getUtilisateur().getIdUtilisateur());
        }

        if (commande.getTicketsVendus() != null) {
            dto.setTicketsVendus(
                commande.getTicketsVendus().stream()
                        .map(TicketVenduMapper::toDTO)
                        .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static Commande toEntity(CommandeDTO dto) {
        if (dto == null)
            return null;

        Commande commande = new Commande();
        commande.setIdCommande(dto.getIdCommande());
        commande.setDateCommande(dto.getDateCommande());
        commande.setStatutCommande(dto.getStatut() != null ? dto.getStatut() : StatutCommande.EN_ATTENTE);
        commande.setTotalCommande(dto.getTotalCommande());

        if (dto.getIdUtilisateur() != null) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setIdUtilisateur(dto.getIdUtilisateur());
            commande.setUtilisateur(utilisateur);
        }

        if (dto.getTicketsVendus() != null) {
            var listeTicketsVendus = dto.getTicketsVendus().stream()
                .map(tvDto -> {
                    TicketVendu tv = TicketVenduMapper.toEntity(tvDto);
                    tv.setCommande(commande); // important pour la relation bidirectionnelle
                    return tv;
                })
                .collect(Collectors.toList());

            commande.setTicketsVendus(listeTicketsVendus);
        }

        return commande;
    }
}