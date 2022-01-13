package com.example.ModelView.controllers;

import com.example.ModelView.services.JsProgressBarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModelRestController {

    private final JsProgressBarService jsProgressBarService;

    @GetMapping(value = "/updateProgressBar")

    public ProgressResponse updateTestBar(){
        return new ProgressResponse(JsProgressBarService.getCurrentCount(), JsProgressBarService.getCurrentTask());
    }
}
