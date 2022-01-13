package com.example.ModelView.controllers;

import lombok.*;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
public class ProgressResponse {

    private Integer currentCount;

    private String currentTask;

}
