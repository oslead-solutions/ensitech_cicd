package com.project.academic_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ObjectAlreadyExistsException extends RuntimeException{
    private String message;

    public ObjectAlreadyExistsException(String msg){
        super(msg);
        this.message = msg;
    }

}
