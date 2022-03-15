package com.example.ModelView.controllers;


import com.example.ModelView.controllers.exceptions.ModelNotFoundException;
import com.example.ModelView.controllers.exceptions.WebSyncGetException;
import com.example.ModelView.controllers.exceptions.WebSyncPostException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<Object> handleModelNotFoundException(
            ModelNotFoundException ex, WebRequest request) {

        System.out.println(ex.getMessage());
        System.out.println(request.toString());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Model not found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebSyncPostException.class)
    public ResponseEntity<Object> handleWebSyncPostException(
            ModelNotFoundException ex, WebRequest request) {

        System.out.println(ex.getMessage());
        System.out.println(request.toString());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Web Sync Post Exception");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebSyncGetException.class)
    public ResponseEntity<Object> handleWebSyncGetException(
            ModelNotFoundException ex, WebRequest request) {

        System.out.println(ex.getMessage());
        System.out.println(request.toString());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Web Sync Get Exception");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(" Change http method type", HttpStatus.NOT_FOUND);
    }
}
