package com.project.user_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.util.Date;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // N'inclut pas les champs nuls dans le JSON
public class ErrorDetails {
    private final Date timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private List<String> details; // Pour les erreurs de validation

    public ErrorDetails(int status, String error, String message, String path) {
        this.timestamp = new Date();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}