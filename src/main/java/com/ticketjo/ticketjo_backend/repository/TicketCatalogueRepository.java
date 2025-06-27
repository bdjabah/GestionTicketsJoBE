package com.ticketjo.ticketjo_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ticketjo.ticketjo_backend.model.TicketCatalogue;

@Repository
public interface TicketCatalogueRepository extends JpaRepository<TicketCatalogue, Long> {

	// Liste des tickets dont le stock est supérieur à une valeur donnée
	List<TicketCatalogue> findByStockGreaterThan(int valeur);

	// Liste des tickets dont le type contient une chaîne (recherche insensible à la casse)
	List<TicketCatalogue> findByTypeTicketContainingIgnoreCase(String type);

}
