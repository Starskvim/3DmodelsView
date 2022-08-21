package com.example.ModelView.model.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintModelZipWeb {

    private String nameModelZip;
    private String modelName;
    private String fileClass;
    private String zipAddress;
    private String zipFormat;
    private Double zipSize;
    private Integer archiveRatio;
}
