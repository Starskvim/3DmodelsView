package com.example.ModelView.dto;


import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class PrintModelDTO {

    private Long id;
    private String modelName;
    private String modelDerictory;
    private String modelCategory;
    private Collection<ModelZIP> modelZIPList;
    private Collection<ModelOTH> modelOTHList;
    private String compressPreview;

}
