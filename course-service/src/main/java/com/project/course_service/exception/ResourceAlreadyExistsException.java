package com.project.course_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException(String courseAlreadyExists) {
        super(courseAlreadyExists);
        System.out.println("Course already exists");

    }
}
