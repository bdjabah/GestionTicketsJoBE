package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.config.StripeProperties;
import com.ticketjo.ticketjo_backend.dto.PaiementDTO;
import com.ticketjo.ticketjo_backend.mapper.PaiementMapper;
import com.ticketjo.ticketjo_backend.model.Commande;
import com.ticketjo.ticketjo_backend.model.Paiement;
import com.ticketjo.ticketjo_backend.model.enums.StatutPaiement;
import com.ticketjo.ticketjo_backend.repository.CommandeRepository;
import com.ticketjo.ticketjo_backend.service.PaiementService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.model.Event;
/**
 * Contrôleur REST pour la gestion des paiements.
 */
/**
 * Contrôleur REST pour la gestion des paiements.
 */
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;
    private final StripeProperties stripeProperties;
    private final CommandeRepository commandeRepository;

    /**
     * Crée un nouveau paiement.
     * 
     * @param paiementDTO Détails du paiement à enregistrer.
     * @return Le paiement créé avec un statut HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<?> creerPaiement(@RequestBody @Valid PaiementDTO dto) {
        try {
            // 🔍 LOG DEBUG
            System.out.println("📨 Paiement reçu :");
            System.out.println("👉 idCommande     : " + dto.getIdCommande());
            System.out.println("👉 idUtilisateur  : " + dto.getIdUtilisateur());
            System.out.println("👉 montant        : " + dto.getMontant());
            System.out.println("👉 statut         : " + dto.getStatut());
            System.out.println("👉 paymentIntentId: " + dto.getPaymentIntentId());

            // Conversion en entité
            Paiement paiement = PaiementMapper.toEntity(dto);

            // ⚠️ LOG AVANT CRÉATION
            if (paiement.getCommande() == null || paiement.getCommande().getIdCommande() == null) {
                System.out.println("❌ ERREUR : Commande manquante dans le paiement !");
            }

            Paiement saved = paiementService.creerPaiement(paiement);
            return new ResponseEntity<>(PaiementMapper.toDTO(saved), HttpStatus.CREATED);

        } catch (Exception e) {
            // 🛑 Erreur attrapée côté backend
            System.out.println("❗ Exception attrapée dans le controller : " + e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Données invalides : " + e.getMessage()));
        }
    }


    /**
     * Récupère un paiement par l'identifiant d'une commande.
     * 
     * @param idCommande ID de la commande associée au paiement.
     * @return Le paiement trouvé ou 404 si absent.
     */
    @GetMapping("/commande/{idCommande}")
    public ResponseEntity<PaiementDTO> trouverParCommande(@PathVariable Long idCommande) {
        Paiement paiement = paiementService.trouverParCommande(idCommande);
        if (paiement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK);
    }

    /**
     * Récupère la liste des paiements en fonction de leur statut.
     * 
     * @param statut Le statut des paiements (ex: EN_ATTENTE, VALIDE, REFUSE).
     * @return Liste des paiements correspondant au statut.
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<PaiementDTO>> listerPaiementsParStatut(@PathVariable StatutPaiement statut) {
        List<PaiementDTO> paiements = paiementService.listerPaiementsParStatut(statut).stream()
                .map(PaiementMapper::toDTO).toList();
        return new ResponseEntity<>(paiements, HttpStatus.OK);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) throws IOException {
        String payload = request.getReader().lines().collect(Collectors.joining());
        String sigHeader = request.getHeader("Stripe-Signature");

        if (sigHeader == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature header manquant");
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
        } catch (Exception e) {
            System.out.println("Signature Stripe invalide : " + e.getMessage());
            return ResponseEntity.badRequest().body("Signature invalide");
        }

        System.out.println("Webhook Stripe reçu : " + event.getType());

        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

        if (intent == null) {
            System.out.println("Échec de la désérialisation du PaymentIntent.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de parsing");
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                paiementService.marquerPaiementValide(intent.getId());
                break;
            case "payment_intent.payment_failed":
                paiementService.marquerPaiementEchoue(intent.getId());
                break;
            default:
                System.out.println("Événement Stripe non traité : " + event.getType());
                break;
        }

        return ResponseEntity.ok("Webhook traité");
    }

    /**
     * Récupère tous les paiements d'un utilisateur donné.
     * 
     * @param idUtilisateur ID de l'utilisateur.
     * @return Liste des paiements liés à cet utilisateur.
     */
    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<List<PaiementDTO>> listerPaiementsUtilisateur(@PathVariable Long idUtilisateur) {
        List<PaiementDTO> paiements = paiementService.listerPaiementsUtilisateur(idUtilisateur).stream()
                .map(PaiementMapper::toDTO).toList();
        return new ResponseEntity<>(paiements, HttpStatus.OK);
    }

    @GetMapping("/intent/{intentId}")
    public ResponseEntity<PaiementDTO> getPaiementByIntentId(@PathVariable String intentId) {
        Paiement paiement = paiementService.trouverParIntentId(intentId);
        if (paiement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK);
    }
}

//@RestController
//@RequestMapping("/api/paiements")
//@RequiredArgsConstructor
//public class PaiementController {
//
//	private final PaiementService paiementService;
//	 private final StripeProperties stripeProperties;
//	/**
//	 * Crée un nouveau paiement.
//	 * 
//	 * @param paiement Détails du paiement à enregistrer.
//	 * @return Le paiement créé avec un statut HTTP 201 (Created).
//	 */
//	@PostMapping
//	public ResponseEntity<PaiementDTO> creerPaiement(@RequestBody @Valid PaiementDTO paiementDTO) {
//		Paiement paiement = PaiementMapper.toEntity(paiementDTO);
//		Paiement createdPaiement = paiementService.creerPaiement(paiement);
//		return new ResponseEntity<>(PaiementMapper.toDTO(createdPaiement), HttpStatus.CREATED);
//	}
//
//	/**
//	 * Récupère un paiement par l'identifiant d'une commande.
//	 * 
//	 * @param idCommande ID de la commande associée au paiement.
//	 * @return Le paiement trouvé ou 404 si absent.
//	 */
//	@GetMapping("/commande/{idCommande}")
//	public ResponseEntity<PaiementDTO> trouverParCommande(@PathVariable Long idCommande) {
//		Paiement paiement = paiementService.trouverParCommande(idCommande);
//		if (paiement == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//		return new ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK);
//	}
//
//	/**
//	 * Récupère la liste des paiements en fonction de leur statut.
//	 * 
//	 * @param statut Le statut des paiements (ex: EN_ATTENTE, VALIDE, REFUSE).
//	 * @return Liste des paiements correspondant au statut.
//	 */
//	@GetMapping("/statut/{statut}")
//	public ResponseEntity<List<PaiementDTO>> listerPaiementsParStatut(@PathVariable StatutPaiement statut) {
//		List<PaiementDTO> paiements = paiementService.listerPaiementsParStatut(statut).stream()
//				.map(PaiementMapper::toDTO).toList();
//		return new ResponseEntity<>(paiements, HttpStatus.OK);
//	}
//	@PostMapping("/webhook")
//	public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) throws IOException {
//	    String payload = request.getReader().lines().collect(Collectors.joining());
//	    String sigHeader = request.getHeader("Stripe-Signature");
//
//	    if (sigHeader == null) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature header manquant");
//	    }
//
//	    Event event;
//	    try {
//	        event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
//	    } catch (Exception e) {
//	        System.out.println("❌ Signature Stripe invalide : " + e.getMessage());
//	        return ResponseEntity.badRequest().body("Signature invalide");
//	    }
//
//	    System.out.println("✅ Webhook Stripe reçu : " + event.getType());
//
//	    PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
//
//	    if (intent == null) {
//	        System.out.println("❌ Échec de la désérialisation du PaymentIntent.");
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de parsing");
//	    }
//
//	    switch (event.getType()) {
//	        case "payment_intent.succeeded":
//	            paiementService.marquerPaiementValide(intent.getId());
//	            break;
//	        case "payment_intent.payment_failed":
//	            paiementService.marquerPaiementEchoue(intent.getId());
//	            break;
//	        default:
//	            System.out.println("⚠️ Événement Stripe non traité : " + event.getType());
//	            break;
//	    }
//
//	    return ResponseEntity.ok("✅ Webhook traité");
//	}
//	/**
//	 * Récupère tous les paiements d'un utilisateur donné.
//	 * 
//	 * @param idUtilisateur ID de l'utilisateur.
//	 * @return Liste des paiements liés à cet utilisateur.
//	 */
//	@GetMapping("/utilisateur/{idUtilisateur}")
//	public ResponseEntity<List<PaiementDTO>> listerPaiementsUtilisateur(@PathVariable Long idUtilisateur) {
//		List<PaiementDTO> paiements = paiementService.listerPaiementsUtilisateur(idUtilisateur).stream()
//				.map(PaiementMapper::toDTO).toList();
//		return new ResponseEntity<>(paiements, HttpStatus.OK);
//	}
//	
//	@GetMapping("/intent/{intentId}")
//	public ResponseEntity<PaiementDTO> getPaiementByIntentId(@PathVariable String intentId) {
//	    Paiement paiement = paiementService.trouverParIntentId(intentId);
//	    if (paiement == null) {
//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	    }
//	    return new ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK);
//	}
//}
/*
 * import com.ticketjo.ticketjo_backend.config.StripeProperties; import
 * com.ticketjo.ticketjo_backend.dto.PaiementDTO; import
 * com.ticketjo.ticketjo_backend.mapper.PaiementMapper; import
 * com.ticketjo.ticketjo_backend.model.Commande; import
 * com.ticketjo.ticketjo_backend.model.Paiement; import
 * com.ticketjo.ticketjo_backend.model.enums.StatutPaiement; import
 * com.ticketjo.ticketjo_backend.repository.CommandeRepository; import
 * com.ticketjo.ticketjo_backend.service.PaiementService;
 * 
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.io.IOException;
 * 
 * import java.util.List; import java.util.stream.Collectors;
 * 
 * import com.stripe.model.PaymentIntent; import com.stripe.net.Webhook; import
 * com.stripe.model.Event;
 *//**
	 * Contrôleur REST pour la gestion des paiements.
	 */
/*
*//**
	 * Contrôleur REST pour la gestion des paiements.
	 */
/*
 * @RestController
 * 
 * @RequestMapping("/api/paiements")
 * 
 * @RequiredArgsConstructor public class PaiementController {
 * 
 * private final PaiementService paiementService; private final StripeProperties
 * stripeProperties; private final CommandeRepository commandeRepository;
 * 
 *//**
	 * Crée un nouveau paiement.
	 * 
	 * @param paiementDTO Détails du paiement à enregistrer.
	 * @return Le paiement créé avec un statut HTTP 201 (Created).
	 */
/*
 * @PostMapping public ResponseEntity<PaiementDTO>
 * creerPaiement(@RequestBody @Valid PaiementDTO paiementDTO) { Paiement
 * paiement = PaiementMapper.toEntity(paiementDTO);
 * 
 * // Association avec la commande avant l'enregistrement if
 * (paiementDTO.getIdCommande() != null) { Commande commande =
 * commandeRepository.findById(paiementDTO.getIdCommande()).orElse(null); if
 * (commande != null) { paiement.setCommande(commande);
 * paiement.setUtilisateur(commande.getUtilisateur()); } }
 * 
 * Paiement createdPaiement = paiementService.creerPaiement(paiement); return
 * new ResponseEntity<>(PaiementMapper.toDTO(createdPaiement),
 * HttpStatus.CREATED); }
 * 
 *//**
	 * Récupère un paiement par l'identifiant d'une commande.
	 * 
	 * @param idCommande ID de la commande associée au paiement.
	 * @return Le paiement trouvé ou 404 si absent.
	 */
/*
 * @GetMapping("/commande/{idCommande}") public ResponseEntity<PaiementDTO>
 * trouverParCommande(@PathVariable Long idCommande) { Paiement paiement =
 * paiementService.trouverParCommande(idCommande); if (paiement == null) {
 * return new ResponseEntity<>(HttpStatus.NOT_FOUND); } return new
 * ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK); }
 * 
 *//**
	 * Récupère la liste des paiements en fonction de leur statut.
	 * 
	 * @param statut Le statut des paiements (ex: EN_ATTENTE, VALIDE, REFUSE).
	 * @return Liste des paiements correspondant au statut.
	 */
/*
 * @GetMapping("/statut/{statut}") public ResponseEntity<List<PaiementDTO>>
 * listerPaiementsParStatut(@PathVariable StatutPaiement statut) {
 * List<PaiementDTO> paiements =
 * paiementService.listerPaiementsParStatut(statut).stream()
 * .map(PaiementMapper::toDTO).toList(); return new ResponseEntity<>(paiements,
 * HttpStatus.OK); }
 * 
 * @PostMapping("/webhook") public ResponseEntity<String>
 * handleStripeWebhook(HttpServletRequest request) throws IOException { String
 * payload = request.getReader().lines().collect(Collectors.joining()); String
 * sigHeader = request.getHeader("Stripe-Signature");
 * 
 * if (sigHeader == null) { return
 * ResponseEntity.status(HttpStatus.BAD_REQUEST).
 * body("Signature header manquant"); }
 * 
 * Event event; try { event = Webhook.constructEvent(payload, sigHeader,
 * stripeProperties.getWebhookSecret()); } catch (Exception e) {
 * System.out.println("Signature Stripe invalide : " + e.getMessage()); return
 * ResponseEntity.badRequest().body("Signature invalide"); }
 * 
 * System.out.println("Webhook Stripe reçu : " + event.getType());
 * 
 * PaymentIntent intent = (PaymentIntent)
 * event.getDataObjectDeserializer().getObject().orElse(null);
 * 
 * if (intent == null) {
 * System.out.println("Échec de la désérialisation du PaymentIntent."); return
 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de parsing"); }
 * 
 * switch (event.getType()) { case "payment_intent.succeeded":
 * paiementService.marquerPaiementValide(intent.getId()); break; case
 * "payment_intent.payment_failed":
 * paiementService.marquerPaiementEchoue(intent.getId()); break; default:
 * System.out.println("Événement Stripe non traité : " + event.getType());
 * break; }
 * 
 * return ResponseEntity.ok("Webhook traité"); }
 * 
 *//**
	 * Récupère tous les paiements d'un utilisateur donné.
	 * 
	 * @param idUtilisateur ID de l'utilisateur.
	 * @return Liste des paiements liés à cet utilisateur.
	 *//*
		 * @GetMapping("/utilisateur/{idUtilisateur}") public
		 * ResponseEntity<List<PaiementDTO>> listerPaiementsUtilisateur(@PathVariable
		 * Long idUtilisateur) { List<PaiementDTO> paiements =
		 * paiementService.listerPaiementsUtilisateur(idUtilisateur).stream()
		 * .map(PaiementMapper::toDTO).toList(); return new ResponseEntity<>(paiements,
		 * HttpStatus.OK); }
		 * 
		 * @GetMapping("/intent/{intentId}") public ResponseEntity<PaiementDTO>
		 * getPaiementByIntentId(@PathVariable String intentId) { Paiement paiement =
		 * paiementService.trouverParIntentId(intentId); if (paiement == null) { return
		 * new ResponseEntity<>(HttpStatus.NOT_FOUND); } return new
		 * ResponseEntity<>(PaiementMapper.toDTO(paiement), HttpStatus.OK); } }
		 */