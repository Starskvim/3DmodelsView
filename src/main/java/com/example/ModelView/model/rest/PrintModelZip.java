package com.example.ModelView.model.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrintModelZip {

    private Long id;
    private String nameModelZIP;
    private String modelName;
    private String fileClass;
    private String modelZIPAddress;
    private String modelZIPFormat;
    private Double sizeZIP;
    private Integer archiveRatio;

}
