package com.ticketjo.ticketjo_backend.mapper;

import com.ticketjo.ticketjo_backend.dto.PanierDTO;
import com.ticketjo.ticketjo_backend.model.Panier;
import com.ticketjo.ticketjo_backend.model.Utilisateur;

/**
 * Classe utilitaire pour convertir entre Panier et PanierDTO. Facilite les
 * échanges entre la couche service et les API.
 */
public class PanierMapper {

	/**
	 * Constructeur privé pour empêcher l'instanciation. Cette classe est purement
	 * utilitaire.
	 */
	private PanierMapper() {
		throw new UnsupportedOperationException("Classe utilitaire");
	}

	/**
	 * Convertit une entité Panier en DTO.
	 *
	 * @param panier L'entité Panier à convertir.
	 * @return Le DTO correspondant.
	 */
	public static PanierDTO toDTO(Panier panier) {
		if (panier == null)
			return null;

		PanierDTO dto = new PanierDTO();
		dto.setIdPanier(panier.getIdPanier());
		dto.setDateCreation(panier.getDateCreation());
		dto.setStatutPanier(panier.getStatutPanier());
		dto.setIdUtilisateur(panier.getUtilisateur() != null ? panier.getUtilisateur().getIdUtilisateur() : null);

		// Conversion des commandes associées
		if (panier.getCommandes() != null) {
			dto.setCommandes(panier.getCommandes().stream().map(CommandeMapper::toDTO).toList());
		}
		return dto;
	}

	/**
	 * Convertit un DTO Panier en entité JPA.
	 *
	 * @param dto Le DTO à convertir.
	 * @return L'entité Panier correspondante.
	 */
	public static Panier toEntity(PanierDTO dto) {
		if (dto == null)
			return null;

		Panier panier = new Panier();
		panier.setIdPanier(dto.getIdPanier());
		panier.setDateCreation(dto.getDateCreation());
		panier.setStatutPanier(dto.getStatutPanier());

		// Rattache l'utilisateur si présent
		if (dto.getIdUtilisateur() != null) {
			Utilisateur u = new Utilisateur();
			u.setIdUtilisateur(dto.getIdUtilisateur());
			panier.setUtilisateur(u);
		}

		// Convertit les commandes du DTO et les rattache au panier
		if (dto.getCommandes() != null) {
			panier.setCommandes(dto.getCommandes().stream().map(CommandeMapper::toEntity).map(cmd -> {
				cmd.setPanier(panier);
				return cmd;
			}).toList());
		}

		return panier;
	}
}