package com.example.RPG_Manager20.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{

    private final HttpStatus status;

    public ResourceNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public ResourceNotFoundException(String resource, Long id, HttpStatus status) {
        super(String.format("%s não encontrado com ID: %d", resource, id));
        this.status = status;
    }

    public ResourceNotFoundException(String resource, String valor, HttpStatus status) {
        super(String.format("%s não encontrado: %s", resource, valor));
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
