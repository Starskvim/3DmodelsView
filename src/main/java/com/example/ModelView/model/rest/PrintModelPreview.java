package com.example.ModelView.model.rest;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModelPreview {

    private Long id;
    private String modelName;
    private String modelDerictory;
    private String modelCategory;
    private String compressPreview;

}
