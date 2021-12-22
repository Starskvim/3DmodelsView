package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "model_other_files")
@NoArgsConstructor
@Setter
@Getter
public class ModelOTH {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameModelOTH;

    private String modelName;

    private String fileClass;

    private String modelOTHAdress;

    private String modelOTHFormat;

    private String sizeOTH;


    public ModelOTH(String nameModelOTH, String modelName, String modelOTHAdress, String modelOTHFormat, String sizeOTH) {
        this.nameModelOTH = nameModelOTH;
        this.modelName = modelName;
        this.fileClass = "OTH";
        this.modelOTHAdress = modelOTHAdress;
        this.modelOTHFormat = modelOTHFormat;
        this.sizeOTH = sizeOTH;
    }


}
