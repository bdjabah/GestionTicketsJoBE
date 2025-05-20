package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.UtilisateurDTO;
import com.ticketjo.ticketjo_backend.mapper.UtilisateurMapper;
import com.ticketjo.ticketjo_backend.model.Utilisateur;
import com.ticketjo.ticketjo_backend.security.JwtUtil;
import com.ticketjo.ticketjo_backend.service.UtilisateurService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

	private final UtilisateurService utilisateurService;
	private final JwtUtil jwtUtil;

	/**
	 * Crée un nouvel utilisateur à partir d'un DTO validé. La méthode utilise un
	 * mapper pour convertir le DTO en entité, puis retourne un DTO en réponse pour
	 * éviter d'exposer l'entité.
	 *
	 * @param utilisateurDTO Les informations de l'utilisateur à créer.
	 * @return L'utilisateur créé avec un statut HTTP 201 (Created).
	 */
	@PostMapping
	public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody @Valid UtilisateurDTO utilisateurDTO) {
		Utilisateur utilisateur = UtilisateurMapper.toEntity(utilisateurDTO);
		Utilisateur created = utilisateurService.createUtilisateur(utilisateur);
		return new ResponseEntity<>(UtilisateurMapper.toDTO(created), HttpStatus.CREATED);
	}

	/**
	 * Supprime un utilisateur existant par son ID.
	 *
	 * @param id L'identifiant de l'utilisateur à supprimer.
	 * @return Une réponse vide avec un statut HTTP 204 (No Content).
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
		utilisateurService.deleteUtilisateur(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Récupère un utilisateur par son adresse email. L'entité est convertie en DTO
	 * avant d'être renvoyée.
	 *
	 * @param email L'email de l'utilisateur à récupérer.
	 * @return L'utilisateur trouvé ou un statut HTTP 404 (Not Found) si inexistant.
	 */
	@GetMapping("/email/{email}")
	public ResponseEntity<UtilisateurDTO> getUtilisateurByEmail(@PathVariable String email) {
		return utilisateurService.getUtilisateurByEmail(email).map(UtilisateurMapper::toDTO)
				.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Récupère la liste de tous les utilisateurs. Les entités sont converties en
	 * DTOs pour l'exposition externe.
	 *
	 * @return Une liste d'utilisateurs avec un statut HTTP 200 (OK).
	 */

	@GetMapping
	public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
		List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs().stream()
				.map(UtilisateurMapper::toDTO).toList(); // Java 16+ version
		return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
	}

	/**
	 * Récupère l'utilisateur actuellement connecté à partir du token JWT.
	 *
	 * @param authHeader L'en-tête Authorization contenant le token JWT.
	 * @return L'utilisateur correspondant, ou 404 si non trouvé.
	 */
	@GetMapping("/me")
	public ResponseEntity<UtilisateurDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
		// Supprimer le préfixe "Bearer " pour obtenir le token brut
		String token = authHeader.replace("Bearer ", "");

		// Extraire l'email depuis le token
		String email = jwtUtil.extractEmail(token);

		// Rechercher l'utilisateur via l'email
		return utilisateurService.getUtilisateurByEmail(email).map(UtilisateurMapper::toDTO)
				.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}