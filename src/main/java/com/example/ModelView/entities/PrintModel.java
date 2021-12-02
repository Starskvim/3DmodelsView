package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "MODELPRINT")
@NoArgsConstructor
@Setter
@Getter
public class PrintModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelName;

    private String modelDerictory;

    private String modelCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private Collection<ModelZIP> modelZIPList = new ArrayList<ModelZIP>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private Collection<ModelOTH> modelOTHList = new ArrayList<ModelOTH>();


    public PrintModel(String modelName, String modelDerictory, String modelCategory) {
        this.modelName = modelName;
        this.modelDerictory = modelDerictory;
        this.modelCategory = modelCategory;
    }

    public void addModelZIP(ModelZIP modelZIP) {
        modelZIPList.add(modelZIP);
    }

    public void addModelOTH(ModelOTH modelOTH) {
        modelOTHList.add(modelOTH);
    }

    public String getOnePicturePreview() {
        String adress = null;
        for (ModelOTH modelOTH : modelOTHList) {
            if (modelOTH.getModelOTHFormat().contains("jpg")) {
                adress = modelOTH.getModelOTHAdress();
            } else if (modelOTH.getModelOTHFormat().contains("png")) {
                adress = modelOTH.getModelOTHAdress();
            } else {
                adress = "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\[aService]\\111.png";
            }
        }
        return adress;
    }



    public String getPreviewBaseSFimg () {

            try {
                String adress = getOnePicturePreview();
                File file =  new File(adress);
                FileInputStream fileInputStreamReader = new FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fileInputStreamReader.read(bytes);
                return new String(Base64.encodeBase64(bytes), "UTF-8");
            } catch (Exception a) {
                return "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\[aService]\\111.png";
            }

    }



}