package com.project.academic_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchAcademicYearExistsException extends RuntimeException{
    private String message;

    public NoSuchAcademicYearExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
