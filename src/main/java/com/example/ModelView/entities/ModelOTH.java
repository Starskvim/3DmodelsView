package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Entity
@Table(name = "model_other_files")
@NoArgsConstructor
@Setter
@Getter
public class ModelOTH implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelOTH modelOTH = (ModelOTH) o;
        return nameModelOTH.equals(modelOTH.nameModelOTH)
                && modelName.equals(modelOTH.modelName)
                && fileClass.equals(modelOTH.fileClass)
                && modelOTHAdress.equals(modelOTH.modelOTHAdress)
                && modelOTHFormat.equals(modelOTH.modelOTHFormat)
                && sizeOTH.equals(modelOTH.sizeOTH);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameModelOTH, modelName, fileClass, modelOTHAdress, modelOTHFormat, sizeOTH);
    }
}
