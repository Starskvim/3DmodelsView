package com.example.ModelView.model.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModelWeb {

    private String modelName;
    private String modelPath;
    private Double modelSize;
    private String modelCategory;
    private Collection<String> modelTagsNames;
    private Collection<PrintModelOthWeb> modelOthList = new ArrayList<>();
    private Collection<PrintModelZipWeb> modelZips = new ArrayList<>();

    @Override
    public String toString() {
        return "PrintModelWebDto{" +
                "modelName='" + modelName + '\'' +
                ", modelPath='" + modelPath + '\'' +
                ", modelSize=" + modelSize +
                ", modelCategory='" + modelCategory + '\'' +
                ", modelTagsNames=" + modelTagsNames +
                ", modelOthList=" + modelOthList +
                '}';
    }
}
