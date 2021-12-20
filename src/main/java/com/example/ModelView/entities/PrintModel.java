package com.example.ModelView.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


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
    private Collection<ModelZIP> modelZIPList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private Collection<ModelOTH> modelOTHList = new ArrayList<>();


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

    ///// !!!!!
    public String getOnePicturePreview() {
        String adress = null;
        for (ModelOTH modelOTH : modelOTHList) {
            if (modelOTH.getModelOTHFormat().contains("jpg") || modelOTH.getModelOTHFormat().contains("png") || modelOTH.getModelOTHFormat().contains("jpeg")) {
                adress = modelOTH.getModelOTHAdress();
            } else {
                adress = "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\[aService]\\111.png";
            }
        }
        return adress;
    }


    public byte[] compression (String adress) throws IOException {

        File input = new File(adress);
        BufferedImage image = ImageIO.read(input);

        return createResultBytes(image);
    }

    private byte[] createResultBytes(BufferedImage losslessimage) {

        float lossyquality = 0.2F;

        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (iter.hasNext()) {
            ImageWriter writer = iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(lossyquality);

            MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream(baos);
            writer.setOutput(mcios);

            // workaround for alpha-channel problems with lossy images: Seems to be non-functional for some pictures, anyway.
            BufferedImage img2=new BufferedImage(losslessimage.getWidth(),losslessimage.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics g=img2.getGraphics();
            g.drawImage(new ImageIcon(losslessimage).getImage(),0,0,null);
            g.dispose();
            IIOImage iioimg = new IIOImage(img2, null, null);
            try {
                writer.write(null, iioimg, iwp);
                mcios.close();
            }
            catch (IOException ioe) {}
        }
        return baos.toByteArray();
    }


    public String getPreviewBaseSFimg () {
        try {

//            String adress = getOnePicturePreview();
//            File file =  new File(adress);
//            FileInputStream fileInputStreamReader = new FileInputStream(file);
//            byte[] bytes = new byte[(int)file.length()];
//            fileInputStreamReader.read(bytes);

            byte[] bytes = compression(getOnePicturePreview());

            return new String(Base64.encodeBase64(bytes), "UTF-8");

        } catch (Exception a) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModel that = (PrintModel) o;
        return id.equals(that.id) && modelName.equals(that.modelName) && modelDerictory.equals(that.modelDerictory) && modelCategory.equals(that.modelCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modelName, modelDerictory, modelCategory);
    }
}