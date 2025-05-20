package com.ticketjo.ticketjo_backend.service.impl;

import com.ticketjo.ticketjo_backend.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

/**
 * Implémentation du service de stockage de fichiers.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path uploadDir = Paths.get("uploads"); // Dossier d'enregistrement

    /**
     * Constructeur : crée le dossier d’upload s’il n’existe pas.
     */
    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d'upload", e);
        }
    }

    /**
     * Enregistre un fichier sur le disque.
     *
     * @param file Fichier à enregistrer.
     * @return Nom du fichier sauvegardé.
     */
    @Override
    public String storeFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();

            // Vérification du nom du fichier
            if (filename == null || filename.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide : " + filename);
            }

            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    /**
     * Renvoie le chemin vers un fichier spécifique.
     *
     * @param fileName Nom du fichier.
     * @return Chemin absolu.
     */
    @Override
    public Path loadFile(String fileName) {
        return uploadDir.resolve(fileName).normalize();
    }

    /**
     * Charge un fichier pour téléchargement.
     *
     * @param fileName Nom du fichier.
     * @return Ressource HTTP.
     */
    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path filePath = loadFile(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Fichier non trouvé : " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur d'URL pour le fichier : " + fileName, e);
        }
    }
}