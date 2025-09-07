package com.project.user_service.exception;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    // Gère ResourceNotFoundException (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        log.warn("Resource not found at [{}]: {}", path, ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), path);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Gère EmailAlreadyExistsException (409 Conflict) - Exemple
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        log.warn("Conflict at [{}]: {}", path, ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(), path);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    // Gère MethodArgumentNotValidException (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String path = request.getDescription(false).substring(4);

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Validation error at [{}]: {}", path, errors);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), "Validation Error", "La validation des données a échoué", path);
        errorDetails.setDetails(errors); // Ajoute la liste des erreurs spécifiques

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Gère toutes les autres exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        log.error("Internal server error at [{}]:", path, ex); // On logue la stack trace complète

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Une erreur interne inattendue est survenue.", path);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}