package com.example.ModelView.rest.response;

import lombok.*;


@Data
@AllArgsConstructor
public class ProgressBarResponse {

    private Integer currentCount;

    private String currentTask;

}
