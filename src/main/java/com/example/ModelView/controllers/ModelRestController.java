package com.example.ModelView.controllers;

import com.example.ModelView.services.JProgressBarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModelRestController {
    private final JProgressBarService jProgressBarService;

    private final TestComponent testComponent;

    @GetMapping(value = "/updateProgressBar", produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateTestBar(){
        return String.valueOf(testComponent.getProgress());
    }
}
