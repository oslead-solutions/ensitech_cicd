package com.project.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée levée lorsqu'une tentative de création d'utilisateur
 * échoue car l'email fourni existe déjà dans la base de données.
 * L'annotation @ResponseStatus indique à Spring de renvoyer un statut HTTP 409 (Conflict)
 * par défaut si cette exception n'est pas gérée par un @ExceptionHandler.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructeur qui prend un message d'erreur détaillé.
     * @param message Le message expliquant la cause de l'erreur.
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}