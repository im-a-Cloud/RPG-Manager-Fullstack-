package com.example.RPG_Manager20.Service;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException{
    private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
