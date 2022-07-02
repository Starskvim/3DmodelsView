package com.example.ModelView.model.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModel {

    private Long id;
    private String modelName;
    private String modelDirectory;
    private String modelCategory;
    private Integer myRate;

}
