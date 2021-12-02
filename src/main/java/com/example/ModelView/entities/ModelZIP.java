package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "MODEL_ZIP_FILES")
@NoArgsConstructor
@Setter
@Getter
public class ModelZIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameModelZIP;

    private String modelZIPAdress;

    private String modelZIPFormat;

    private String sizeZIP;

    private double archiveRatio;

    //@ManyToOne
    //@JoinColumn
    //private PrintModel printModell;


    public ModelZIP (String nameModelZIP, String modelZIPAdress, String modelZIPFormat, String sizeZIP, double archiveRatio){
        this.nameModelZIP = nameModelZIP;
        this.modelZIPAdress = modelZIPAdress;
        this.modelZIPFormat = modelZIPFormat;
        this.sizeZIP = sizeZIP;
        this.archiveRatio = archiveRatio;
    }
}
