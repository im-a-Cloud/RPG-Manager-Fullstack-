package com.example.RPG_Manager20.Model.Enums;

import java.text.MessageFormat;

public enum ErrorMessageUtils {
    ERROR_NOT_FOUND("{0} not found."),
    ERROR_ALREADY_EXISTS("{0} already exists with this {1}.");
    private final String template;

    ErrorMessageUtils(String template) {
        this.template = template;
    }

    public String getMessage(Object... args) {
        return MessageFormat.format(template, args);
    }
}
