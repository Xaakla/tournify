package com.app.tournify.exceptions.errors;

public class NotFoundException extends RuntimeException{
    public NotFoundException (String errorMessage){
        super(errorMessage);
    }
}