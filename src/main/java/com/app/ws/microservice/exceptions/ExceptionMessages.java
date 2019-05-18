package com.app.ws.microservice.exceptions;

public enum ExceptionMessages {
    MISSING_REQUIRED_FIELDS("Missing required fields.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
