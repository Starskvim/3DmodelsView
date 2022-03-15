package com.example.ModelView.controllers.exceptions;

public class WebSyncPostException extends RuntimeException{

    public WebSyncPostException(String modelName){
        super(String.format("Model with Id %s not post on Web", modelName));
    }

}
