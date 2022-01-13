package com.example.ModelView.controllers;

import com.example.ModelView.services.JsProgressBarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModelRestController {

    private final JsProgressBarService jsProgressBarService;

    @GetMapping(value = "/updateProgressBar")

    public ProgressResponse updateTestBar(){

        ProgressResponse progressResponse = new ProgressResponse(JsProgressBarService.getCurrentCount(), JsProgressBarService.getCurrentTask());

        System.out.println(progressResponse);
        return progressResponse;
    }
}
