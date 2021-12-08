package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "model_zip_files")
@NoArgsConstructor
@Setter
@Getter
public class ModelZIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameModelZIP;

    private String modelName;

    private String fileClass;

    private String modelZIPAdress;

    private String modelZIPFormat;

    private String sizeZIP;

    private double archiveRatio;



    public ModelZIP (String nameModelZIP,String modelName, String modelZIPAdress, String modelZIPFormat, String sizeZIP, double archiveRatio){
        this.nameModelZIP = nameModelZIP;
        this.modelName = modelName;
        this.fileClass = "ZIP";
        this.modelZIPAdress = modelZIPAdress;
        this.modelZIPFormat = modelZIPFormat;
        this.sizeZIP = sizeZIP;
        this.archiveRatio = archiveRatio;
    }
}
