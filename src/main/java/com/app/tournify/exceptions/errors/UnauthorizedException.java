package com.app.tournify.exceptions.errors;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException (String errorMessage){
        super(errorMessage);
    }
}