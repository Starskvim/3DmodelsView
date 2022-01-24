package com.example.ModelView.dto.web;


import com.example.ModelView.entities.ModelOTH;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModelWebDTO {

    private Long id;
    private String modelName;
    private String modelDerictory;
    private String modelCategory;
    private List<String> modelTags;
    private String compressedPreview;
    private Double totalSize;
    private Collection<ModelOTHWebDTO> modelOTHList = new ArrayList<>();

}
