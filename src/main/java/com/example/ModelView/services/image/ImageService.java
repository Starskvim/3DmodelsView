package com.example.ModelView.services.image;

import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.example.ModelView.utillity.Constant.Create.IMAGE_FORMATS_TRIGGERS;
import static com.example.ModelView.utillity.CreateUtils.detectTrigger;
import static java.util.Objects.nonNull;

@Service
public class ImageService {


    public String getPreviewBase64Img(PrintModelData printModelData, Boolean comression) {
        try {
            if (comression){
                byte[] bytes = compression(getOnePicturePreview(printModelData), 0.2f);
                return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
            }
        } catch (Exception a) {
            return null;
        }
        return null;
    }

    public String getBase64ImgWeb(PrintModelOthData printModelOthData, Boolean compression, float quality) {
        try {
            if (compression){
                byte[] bytes = compression(getPicture(printModelOthData), quality);
                return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
            } else {
                return getFullBase64Img(printModelOthData);
            }
        } catch (Exception a) {
            return null;
        }
    }

    public String getFullBase64Img(PrintModelOthData printModelOthData) {

        try (FileInputStream fileInputStreamReader = new FileInputStream(getPicture(printModelOthData))) {
            File file =  new File(getPicture(printModelOthData));
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private String getPicture(PrintModelOthData printModelOthData) {
        String address;
        String format = printModelOthData.getOthFormat();
        if (detectTrigger(format, IMAGE_FORMATS_TRIGGERS)) {
            address = printModelOthData.getOthAddress();
        } else {
            address = "";
        }
        return address;
    }


    private String getOnePicturePreview(PrintModelData printModelData) {
        String adress = null;
        for (PrintModelOthData printModelOthData : printModelData.getPrintModelOthDataSet()) {
            if (detectTrigger(printModelOthData.getOthFormat(), IMAGE_FORMATS_TRIGGERS)) {
                adress = printModelOthData.getOthAddress();
            } else {
                adress = "";
            }
        }
        return adress;
    }


    private byte[] compression (String address, float quality) throws IOException {
        if(nonNull(address)) {
            File input = new File(address);
            BufferedImage image = ImageIO.read(input);
            return createResultBytes(image, quality);
        }
        return null;
    }



    private byte[] createResultBytes(BufferedImage losslessImage, float quality) {

        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (iter.hasNext()) {
            ImageWriter writer = iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);

            MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream(baos);
            writer.setOutput(mcios);

            // workaround for alpha-channel problems with lossy images: Seems to be non-functional for some pictures, anyway.
            BufferedImage img2=new BufferedImage(losslessImage.getWidth(), losslessImage.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics g=img2.getGraphics();
            g.drawImage(new ImageIcon(losslessImage).getImage(),0,0,null);
            g.dispose();
            IIOImage iioimg = new IIOImage(img2, null, null);
            try {
                writer.write(null, iioimg, iwp);
                mcios.close();
            }
            catch (IOException ioe) {
                System.out.println(ioe.getMessage());
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
