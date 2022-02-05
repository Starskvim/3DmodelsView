package com.example.ModelView.dto.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModelWebDTO implements Serializable {
    private String modelName;
    private String modelPath;
    private Double modelSize;
    private String modelCategory;
    private Collection<String> modelTagsNames;
    private Collection<ModelOTHWebDTO> modelOTHList = new ArrayList<>();

    @Override
    public String toString() {
        return "PrintModelWebDTO{" +
                "modelName='" + modelName + '\'' +
                ", modelPath='" + modelPath + '\'' +
                ", modelSize=" + modelSize +
                ", modelCategory='" + modelCategory + '\'' +
                ", modelTagsNames=" + modelTagsNames +
                ", modelOTHList=" + modelOTHList +
                '}';
    }
}
