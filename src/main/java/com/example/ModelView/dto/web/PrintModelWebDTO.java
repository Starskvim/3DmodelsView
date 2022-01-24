package com.example.ModelView.dto.web;


import com.example.ModelView.entities.ModelOTH;
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

    private Long id;
    private String modelName;
    private String modelDerictory;
    private String modelCategory;
    private List<String> modelTags;
    private String compressedPreview;
    private Double totalSize;
    private Collection<ModelOTHWebDTO> modelOTHList = new ArrayList<>();

}
