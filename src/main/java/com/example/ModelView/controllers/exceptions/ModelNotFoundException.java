package com.example.ModelView.controllers.exceptions;

public class ModelNotFoundException extends RuntimeException{

    public ModelNotFoundException(Long id) {
        super(String.format("Model with Id %d not found", id));
    }

}
