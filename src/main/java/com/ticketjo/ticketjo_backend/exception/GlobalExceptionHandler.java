package com.ticketjo.ticketjo_backend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les erreurs de validation (annotations @Valid).
     * Capture les erreurs champ par champ et les renvoie dans un format lisible.
     *
     * @param ex L'exception capturée.
     * @return Map des erreurs de validation (champ -> message).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs de type IllegalArgumentException.
     * Typiquement utilisé pour signaler un mauvais argument métier (ex: doublon email).
     *
     * @param ex L'exception levée.
     * @return Map contenant un seul message d'erreur.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toute autre exception non spécifiquement attrapée.
     * Permet d’éviter d’exposer les détails internes du serveur au client.
     *
     * @param ex L'exception levée.
     * @return Message d’erreur générique avec statut 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>("Erreur interne du serveur : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}