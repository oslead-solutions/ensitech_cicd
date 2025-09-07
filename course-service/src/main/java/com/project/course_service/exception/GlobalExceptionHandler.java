package com.project.course_service.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gère de manière centralisée les exceptions pour toute l'application.
 * Transforme les exceptions en réponses HTTP structurées (JSON) pour le client.
 */
@ControllerAdvice
public class GlobalExceptionHandler { // Ne doit pas hériter de RuntimeException

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère l'exception quand une ressource n'est pas trouvée (404 Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        log.warn("Resource not found at path [{}]: {}", path, ex.getMessage());
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), path);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * NOUVEAU : Gère les conflits de création (409 Conflict).
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        log.warn("Conflict error at path [{}]: {}", path, ex.getMessage());
        Map<String, Object> body = createErrorBody(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), path);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Gère les erreurs de validation des DTOs dans le corps de la requête (400 Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        log.warn("Validation error for request at path [{}]: {}", path, errors);
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, "Validation Error", "La validation des données a échoué", path);
        body.put("details", errors); // 'details' est plus standard que 'errors'
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs de validation des paramètres de requête (400 Bad Request).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        log.warn("Constraint violation at path [{}]: {}", path, errors);
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, "Constraint Violation", "Une contrainte a été violée", path);
        body.put("details", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions non prévues (500 Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        log.error("Unhandled internal server error at path [{}]:", path, ex);
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Une erreur interne est survenue. Veuillez contacter l'administrateur.", path);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Méthode utilitaire pour créer le corps de la réponse d'erreur.
     */
    private Map<String, Object> createErrorBody(HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}