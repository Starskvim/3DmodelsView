package com.example.ModelView.rest;

import com.example.ModelView.rest.response.DBStatsResponse;
import com.example.ModelView.rest.response.ProgressBarResponse;
import com.example.ModelView.persistance.JdbcTemplateDBStats;
import com.example.ModelView.services.JsProgressBarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModelRestController {
    private final JsProgressBarService jsProgressBarService;

    private final JdbcTemplateDBStats jdbcTemplateDBStats;

    @GetMapping(value = "/updateProgressBar")
    public ProgressBarResponse updateTestBar(){
        return new ProgressBarResponse(JsProgressBarService.getCurrentCount(), JsProgressBarService.getCurrentTask());
    }

    @GetMapping(value = "/stats")
    public DBStatsResponse getStats(){
        return jdbcTemplateDBStats.getStats();
    }
}
