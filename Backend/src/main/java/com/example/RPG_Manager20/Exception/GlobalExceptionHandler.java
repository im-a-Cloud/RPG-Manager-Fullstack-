package com.example.RPG_Manager20.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.example.RPG_Manager20.Exception.ExceptionDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Erro de validação");

        String path = request.getDescription(false).replace("uri=", "");

        ExceptionDTO exceptionDTO = new ExceptionDTO(
                HttpStatus.BAD_REQUEST.value(),
                mensagem,
                LocalDateTime.now().toString(),
                path
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }
}