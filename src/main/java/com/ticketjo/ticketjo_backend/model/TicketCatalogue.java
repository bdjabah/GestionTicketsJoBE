package com.ticketjo.ticketjo_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les champs
@Entity
public class TicketCatalogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    private String typeTicket;
    private double prixTicket;
    private Integer stock;
    private Integer capacite;
  
    private String imageTicket;
    
    public String getStatutTicket() {
    	return this.stock != null && this.stock > 0 ? "DISPONIBLE" : "EPUISE";
    }
}

    
