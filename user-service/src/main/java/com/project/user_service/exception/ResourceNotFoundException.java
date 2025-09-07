package com.project.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'une ressource (ex: Enseignant, Étudiant) n'est pas trouvée dans la base de données.
 * L'annotation @ResponseStatus est un fallback, mais nous allons la gérer plus proprement dans le @ControllerAdvice.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
