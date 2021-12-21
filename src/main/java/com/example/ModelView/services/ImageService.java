package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.PrintModel;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Service
public class ImageService {


    public String getPreviewBaseSFimg (PrintModel printModel, Boolean comression) {
        try {


            if (comression){
                byte[] bytes = compression(getOnePicturePreview(printModel));
                return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
            }

//            else {
//
////                String adress = getOnePicturePreview();
////                File file =  new File(adress);
////                FileInputStream fileInputStreamReader = new FileInputStream(file);
////                byte[] bytes = new byte[(int)file.length()];
////                fileInputStreamReader.read(bytes);
//            }

        } catch (Exception a) {
            return null;
        }

        return null;
    }

//    public String getPicture() {
//        String adress;
//        if (this.modelOTHFormat.contains("jpg") || this.modelOTHFormat.contains("png") || this.modelOTHFormat.contains("jpeg")) {
//            adress = this.modelOTHAdress;
//        } else {
//            adress = "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\[aService]\\111.png";
//        }
//        return adress;
//    }


    public String getOnePicturePreview(PrintModel printModel) {
        String adress = null;
        for (ModelOTH modelOTH : printModel.getModelOTHList()) {
            if (modelOTH.getModelOTHFormat().contains("jpg") || modelOTH.getModelOTHFormat().contains("png") || modelOTH.getModelOTHFormat().contains("jpeg")) {
                adress = modelOTH.getModelOTHAdress();
            } else {
                adress = "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\[aService]\\111.png";
            }
        }
        return adress;
    }


    private byte[] compression (String adress) throws IOException {

        File input = new File(adress);
        BufferedImage image = ImageIO.read(input);
        return createResultBytes(image);

    }



    private byte[] createResultBytes(BufferedImage losslessimage) {

        float lossyquality = 0.5F;

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
            catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
        return baos.toByteArray();
    }

//    public byte[] getLossyImage(BufferedImage losslessimage,int maxsize,float maxquality) {
//        byte[] resultbytes=createResultBytes(losslessimage,maxquality);
//        if (resultbytes.length>size) {
//            int minsize=(int)(size*.9f);
//            float newq=maxquality/2f;
//            resultbytes=createResultBytes(losslessimage,newq);
//            float qdiff=newq/2f;
//            while ((resultbytes.length<minsize || resultbytes.length>size) &&  diff>.02f) {
//                newq+=(resultbytes.length<minsize?qdiff:-qdiff);
//                resultbytes=createResultBytes(losslessimage,newq);
//                qdiff=qdiff/2f;
//            }
//        }
//        return resultbytes;
//    }

}