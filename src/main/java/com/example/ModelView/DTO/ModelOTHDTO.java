package com.example.ModelView.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModelOTHDTO {
    private Long id;
    private String nameModelOTH;
    private String modelName;
    private String fileClass;
    private String modelOTHAdress;
    private String modelOTHFormat;
    private Double sizeOTH;

    private String fullPreview;
}


