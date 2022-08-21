package com.example.ModelView.model.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModelOthWeb {

    private String nameModelOTH;
    private String modelName;
    private String fileClass;
    private String modelOTHFormat;
    private Double sizeOTH;
    private String previewOth;

    @Override
    public String toString() {
        return "ModelOTHWebDTO{" +
                "nameModelOTH='" + nameModelOTH + '\'' +
                ", modelName='" + modelName + '\'' +
                ", fileClass='" + fileClass + '\'' +
                ", modelOTHFormat='" + modelOTHFormat + '\'' +
                ", sizeOTH=" + sizeOTH +
                ", previewOth='" + previewOth + '\'' +
                '}';
    }
}
