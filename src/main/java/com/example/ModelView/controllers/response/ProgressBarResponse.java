package com.example.ModelView.controllers.response;

import lombok.*;


@Data
@AllArgsConstructor
public class ProgressBarResponse {

    private Integer currentCount;

    private String currentTask;

}
