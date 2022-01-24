package com.example.ModelView.dto.web;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelOTHWebDTO implements Serializable {
    private Long id;
    private String nameModelOTH;
    private String modelName;
    private String fileClass;
    private String modelOTHAdress;
    private String modelOTHFormat;
    private Double sizeOTH;
    private String fullPreview;
}
