package com.example.RPG_Manager20.Exception;

public class ExceptionDTO {
    private int status;
    private String message;
    private String timestamp;
    private String path;

    public ExceptionDTO() {}

    public ExceptionDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ExceptionDTO(int status, String message, String timestamp, String path) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }

    // Getters e Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}