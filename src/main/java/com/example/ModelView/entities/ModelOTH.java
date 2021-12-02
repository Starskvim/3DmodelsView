package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.io.*;

@Entity
@Table (name = "MODEL_OTHER_FILES")
@NoArgsConstructor
@Setter
@Getter
public class ModelOTH {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameModelOTH;

    private String modelOTHAdress;

    private String modelOTHFormat;

    private String sizeOTH;

    //@ManyToOne
    //@JoinColumn
    //private PrintModel printModell;



    public ModelOTH (String nameModelOTH, String modelOTHAdress, String modelOTHFormat, String sizeOTH){
        this.nameModelOTH = nameModelOTH;
        this.modelOTHAdress = modelOTHAdress;
        this.modelOTHFormat = modelOTHFormat;
        this.sizeOTH = sizeOTH;
    }


    public String getBaseSFimg () throws IOException {

        String adress = this.modelOTHAdress;

        File file =  new File(adress);
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);



        return new String(Base64.encodeBase64(bytes), "UTF-8");

    }


}
