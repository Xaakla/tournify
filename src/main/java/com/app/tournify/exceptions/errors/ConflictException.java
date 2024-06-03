package com.app.tournify.exceptions.errors;

public class ConflictException extends RuntimeException {
    public ConflictException(String errorMessage) {
        super(errorMessage);
    }
}