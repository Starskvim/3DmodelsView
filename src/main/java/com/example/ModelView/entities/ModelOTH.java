package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.io.*;

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


    public String getPicture() {
        String adress;
        if (this.modelOTHFormat.contains("jpg") || this.modelOTHFormat.contains("png") || this.modelOTHFormat.contains("jpeg")) {
            adress = this.modelOTHAdress;
        } else {
            adress = "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\[aService]\\111.png";
        }
        return adress;
    }

    public String getBaseSFimg() throws IOException {

        String adress = getPicture();

        File file = new File(adress);
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);

        return new String(Base64.encodeBase64(bytes), "UTF-8");

    }


}
