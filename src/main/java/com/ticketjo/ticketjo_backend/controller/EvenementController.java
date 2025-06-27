package com.ticketjo.ticketjo_backend.controller;

import com.ticketjo.ticketjo_backend.dto.EvenementDTO;
import com.ticketjo.ticketjo_backend.mapper.EvenementMapper;
import com.ticketjo.ticketjo_backend.model.Evenement;
import com.ticketjo.ticketjo_backend.service.EvenementService;
import com.ticketjo.ticketjo_backend.service.FileStorageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des événements, y compris l'upload d'image.
 */

@RestController
@RequestMapping("/api/evenements")
@RequiredArgsConstructor
public class EvenementController {

    private final EvenementService evenementService;
    private final FileStorageService fileStorageService;

    /**
     * Crée un nouvel événement.
     *
     * @param evenementDTO Les données de l'événement à créer.
     * @param image  Le fichier image associé.
     * @return L'événement créé sous forme de DTO avec statut HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<EvenementDTO> creerEvenement(@RequestBody @Valid EvenementDTO evenementDTO) {
        Evenement evenement = EvenementMapper.toEntity(evenementDTO);
        Evenement created = evenementService.creerEvenement(evenement);
        return new ResponseEntity<>(EvenementMapper.toDTO(created), HttpStatus.CREATED);
    }

    /**
     * Récupère tous les événements disponibles.
     *
     * @return Liste complète des événements (DTO) avec statut HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<EvenementDTO>> getAllEvenements() {
        List<EvenementDTO> dtos = evenementService.getAllEvenements().stream()
                .map(EvenementMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Met à jour un événement existant identifié par son ID.
     *
     * @param id            ID de l'événement à mettre à jour.
     * @param evenementDTO  Données modifiées de l'événement.
     * @return L'événement mis à jour sous forme de DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvenementDTO> updateEvenement(@PathVariable Long id,
                                                        @RequestBody @Valid EvenementDTO evenementDTO) {
        evenementDTO.setIdEvenement(id);
        Evenement updated = evenementService.updateEvenement(EvenementMapper.toEntity(evenementDTO));
        return ResponseEntity.ok(EvenementMapper.toDTO(updated));
    }

    /**
     * Supprime un événement par son ID.
     *
     * @param id ID de l'événement à supprimer.
     * @return Réponse vide avec statut HTTP 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.supprimerEvenement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Recherche les événements par nom (partiel ou complet).
     *
     * @param nomEvenement Le nom de l'événement à rechercher.
     * @return Liste d'événements correspondant au nom.
     */
    @GetMapping("/nom/{nomEvenement}")
    public ResponseEntity<List<EvenementDTO>> rechercherParNom(@PathVariable String nomEvenement) {
        List<EvenementDTO> dtos = evenementService.rechercherParNom(nomEvenement).stream()
                .map(EvenementMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Recherche les événements par discipline.
     *
     * @param discipline Discipline à filtrer (ex : Natation).
     * @return Liste des événements associés à cette discipline.
     */
    @GetMapping("/discipline/{discipline}")
    public ResponseEntity<List<EvenementDTO>> rechercherParDiscipline(@PathVariable String discipline) {
        List<EvenementDTO> dtos = evenementService.rechercherParDiscipline(discipline).stream()
                .map(EvenementMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Recherche les événements à une date spécifique.
     *
     * @param dateEvenement Date cible au format YYYY-MM-DD.
     * @return Liste des événements ayant lieu à cette date.
     */
    @GetMapping("/date/{dateEvenement}")
    public ResponseEntity<List<EvenementDTO>> rechercherParDate(@PathVariable String dateEvenement) {
        LocalDate date = LocalDate.parse(dateEvenement);
        List<EvenementDTO> dtos = evenementService.rechercherParDate(date).stream()
                .map(EvenementMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Recherche les événements organisés à un lieu donné.
     *
     * @param lieuEvenement Le lieu à rechercher (ex : Paris).
     * @return Liste d'événements correspondant au lieu.
     */
    @GetMapping("/lieu/{lieuEvenement}")
    public ResponseEntity<List<EvenementDTO>> rechercherParLieu(@PathVariable String lieuEvenement) {
        List<EvenementDTO> dtos = evenementService.rechercherParLieu(lieuEvenement).stream()
                .map(EvenementMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    @PostMapping(
    		  path = "/upload",
    		  consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    		)
    		public ResponseEntity<EvenementDTO> createWithUpload(
    			@ModelAttribute @Valid EvenementDTO dto,
    		    @RequestPart(value = "image", required = false) MultipartFile image
    		) {
    		    // Si vous voulez logger les erreurs de validation, vous pouvez capturer BindingResult
    		    // et renvoyer les messages détaillés.

    		    //  Sauvegarde de l’image si présente
    		    if (image != null && !image.isEmpty()) {
    		        String imageUrl = fileStorageService.storeFile(image);
    		        dto.setImageUrl(imageUrl);
    		    }

    		    //  Création de l’entité et persistance
    		    Evenement saved = evenementService.creerEvenement(EvenementMapper.toEntity(dto));

    		    //  Retour DTO + 201 Created
    		    return ResponseEntity
    		            .status(HttpStatus.CREATED)
    		            .body(EvenementMapper.toDTO(saved));
    		}
    /**
     * Téléchargement d’un fichier stocké pour un événement.
     *
     * @param fileName Nom du fichier à télécharger.
     * @return Le fichier en tant que ressource HTTP.
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFile(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}