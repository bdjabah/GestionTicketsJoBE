package com.ticketjo.ticketjo_backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Email déjà utilisé");

        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Email déjà utilisé", response.getBody().get("error"));
    }

    @Test
    void testHandleAllExceptions() {
        Exception ex = new Exception("Quelque chose a explosé");

        ResponseEntity<String> response = handler.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Quelque chose a explosé"));
    }

    @Test
    void testHandleValidationExceptions() {
        // Préparer le mock pour BindingResult
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("utilisateur", "email", "Email invalide");
        FieldError error2 = new FieldError("utilisateur", "nom", "Nom requis");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Email invalide", response.getBody().get("email"));
        assertEquals("Nom requis", response.getBody().get("nom"));
    }
}
