package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.TicketVenduDTO;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;
import com.ticketjo.ticketjo_backend.model.TicketVendu;
import com.ticketjo.ticketjo_backend.model.Utilisateur;

	public class TicketVenduMapper {

	    private TicketVenduMapper() {
	        throw new UnsupportedOperationException("Classe utilitaire");
	    }

	    // --- ENTITÉ → DTO ---
	    public static TicketVenduDTO toDTO(TicketVendu entity) {
	        if (entity == null) return null;

	        TicketVenduDTO dto = new TicketVenduDTO();
	        dto.setIdTicketVendu(entity.getIdTicketVendu());
	        dto.setCleTicket(entity.getCleTicket());
	        dto.setNom(entity.getNom());
	        dto.setPrenom(entity.getPrenom());
	        dto.setQrCode(entity.getQrCode());
	        dto.setDateAchat(entity.getDateAchat());
	        dto.setTypeTicket(entity.getTypeTicket());
	        dto.setPrixTicket(entity.getPrixTicket());
	        dto.setImageTicket(entity.getImageTicket());

	        if (entity.getUtilisateur() != null) {
	            dto.setIdUtilisateur(entity.getUtilisateur().getIdUtilisateur());
	        }

	        if (entity.getCommande() != null) {
	            dto.setIdCommande(entity.getCommande().getIdCommande());
	        }

	        if (entity.getTicketCatalogue() != null) {
	            dto.setIdTicketCatalogue(entity.getTicketCatalogue().getIdTicket());
	        }

	        if (entity.getStatutTicket() != null) {
	            dto.setStatutTicket(entity.getStatutTicket()); // ✅ plus de .name()
	        }

	        return dto;
	    }

	    // --- DTO → ENTITÉ ---
	    public static TicketVendu toEntity(TicketVenduDTO dto) {
	        if (dto == null) return null;

	        TicketVendu entity = new TicketVendu();
	        entity.setIdTicketVendu(dto.getIdTicketVendu());
	        entity.setCleTicket(dto.getCleTicket());
	        entity.setNom(dto.getNom());
	        entity.setPrenom(dto.getPrenom());
	        entity.setQrCode(dto.getQrCode());
	        entity.setDateAchat(dto.getDateAchat());
	        entity.setTypeTicket(dto.getTypeTicket());
	        entity.setPrixTicket(dto.getPrixTicket());
	        entity.setImageTicket(dto.getImageTicket());

	        if (dto.getIdUtilisateur() != null) {
	            Utilisateur utilisateur = new Utilisateur();
	            utilisateur.setIdUtilisateur(dto.getIdUtilisateur());
	            entity.setUtilisateur(utilisateur);
	        }

	        if (dto.getIdCommande() != null) {
	            Commande commande = new Commande();
	            commande.setIdCommande(dto.getIdCommande());
	            entity.setCommande(commande);
	        }

	        if (dto.getIdTicketCatalogue() != null) {
	            TicketCatalogue catalogue = new TicketCatalogue();
	            catalogue.setIdTicket(dto.getIdTicketCatalogue());
	            entity.setTicketCatalogue(catalogue);
	        }

	        if (dto.getStatutTicket() != null) {
	            entity.setStatutTicket(dto.getStatutTicket()); // ✅ plus de valueOf()
	        }

	        return entity;
	    }
	}