package com.example.ModelView.controllers;

import com.example.ModelView.controllers.response.DBStatsResponse;
import com.example.ModelView.controllers.response.ProgressBarResponse;
import com.example.ModelView.repositories.JdbcTemplateDBStatsDao;
import com.example.ModelView.services.JsProgressBarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModelRestController {

    private final JsProgressBarService jsProgressBarService;

    private final JdbcTemplateDBStatsDao jdbcTemplateDBStatsDao;

    @GetMapping(value = "/updateProgressBar")
    public ProgressBarResponse updateTestBar(){
        return new ProgressBarResponse(JsProgressBarService.getCurrentCount(), JsProgressBarService.getCurrentTask());
    }

    @GetMapping(value = "/stats")
    public DBStatsResponse getStats(){
        return jdbcTemplateDBStatsDao.getStats();
    }
}
