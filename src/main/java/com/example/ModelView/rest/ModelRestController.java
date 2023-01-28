package com.example.ModelView.rest;

import com.example.ModelView.rest.response.DataBaseStatsView;
import com.example.ModelView.rest.response.ProgressBarResponse;
import com.example.ModelView.persistance.JdbcTemplateService;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.lokal.SerializeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ModelRestController {
    private final JsProgressBarService jsProgressBarService;

    private final JdbcTemplateService jdbcTemplateServiceDBStats;
    private final SerializeService serializeService;

    @GetMapping(value = "/update-progress-bar")
    public ProgressBarResponse updateTestBar(){
        return new ProgressBarResponse(JsProgressBarService.getCurrentCount(), JsProgressBarService.getCurrentTask());
    }

    @GetMapping(value = "/stats")
    public DataBaseStatsView getStats(){
        return jdbcTemplateServiceDBStats.getStats();
    }

    @PostMapping(value = "/admin/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        serializeService.handleFileUploadService(file);
        return "redirect:/admin";
    }
}
