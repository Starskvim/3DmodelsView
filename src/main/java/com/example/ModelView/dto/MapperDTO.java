package com.example.ModelView.dto;

import com.example.ModelView.dto.web.ModelOTHWebDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Component
@AllArgsConstructor
public class MapperDTO {

    private final ImageService imageService;

    public PrintModelDTO toPrintModelDTO(PrintModel printModel){
        Long id = printModel.getId();
        String modelName = printModel.getModelName();
        String modelDerictory = printModel.getModelDerictory();
        String modelCategory = printModel.getModelCategory();
        Collection<ModelZIP> modelZIPList = printModel.getModelZIPSet();
        Collection<ModelOTH> modelOTHList = printModel.getModelOTHSet();
        String compressPreview = imageService.getPreviewBaseSFimg(printModel, true);

        return new PrintModelDTO(id, modelName, modelDerictory, modelCategory, modelZIPList, modelOTHList, compressPreview);
    }

    public ModelOTHDTO toModelOTHDTO(ModelOTH modelOTH){

        Long id = modelOTH.getId();
        String nameModelOTH = modelOTH.getNameModelOTH();
        String modelName = modelOTH.getModelName();
        String fileClass = modelOTH.getFileClass();
        String modelOTHAdress = modelOTH.getModelOTHAdress();
        String modelOTHFormat = modelOTH.getModelOTHFormat();
        Double sizeOTH = modelOTH.getSizeOTH();
        String fullPreview = imageService.getFullBaseSFimg(modelOTH);

        return new ModelOTHDTO(id, nameModelOTH, modelName, fileClass, modelOTHAdress, modelOTHFormat, sizeOTH, fullPreview);
    }

    public PrintModelWebDTO toPrintModelWebDTO (PrintModel printModel){

        PrintModelWebDTO newPrintModelWebDTO = new PrintModelWebDTO();
        newPrintModelWebDTO.setModelName(printModel.getModelName());
        newPrintModelWebDTO.setModelDerictory(printModel.getModelDerictory());
        newPrintModelWebDTO.setModelCategory(printModel.getModelCategory());
        newPrintModelWebDTO.setModelTags(printModel.getModelTags());

        Set<ModelOTH> modelOTHSet = printModel.getModelOTHSet();
        for(ModelOTH modelOTH : modelOTHSet){
            String format = modelOTH.getModelOTHFormat();
            if(format.equals("jpg") || format.equals("jpeg") || format.equals("png")){
                newPrintModelWebDTO.setCompressedPreview(imageService.getBaseSFimgWeb(modelOTH, true, 0.2f));
                break;
            }
        }
        ArrayList<ModelOTHWebDTO> resiltList = new ArrayList<>();
        for(ModelOTH modelOTH : modelOTHSet){
            resiltList.add(toModelOTHWebDTO(modelOTH));
        }

        newPrintModelWebDTO.setModelOTHList(resiltList);

        Double totalSize = 0d;
        for (ModelZIP modelZIP: printModel.getModelZIPSet()){
            totalSize += modelZIP.getSizeZIP();
        }

        newPrintModelWebDTO.setTotalSize(totalSize);

        return newPrintModelWebDTO;
    }

    public ModelOTHWebDTO toModelOTHWebDTO (ModelOTH modelOTH){

        ModelOTHWebDTO newModelOTHWebDTO = new ModelOTHWebDTO();

        newModelOTHWebDTO.setNameModelOTH(modelOTH.getNameModelOTH());
        newModelOTHWebDTO.setModelName(modelOTH.getModelName());
        newModelOTHWebDTO.setFileClass(modelOTH.getFileClass());
        newModelOTHWebDTO.setModelOTHAdress(modelOTH.getModelOTHAdress());
        newModelOTHWebDTO.setModelOTHFormat(modelOTH.getModelOTHFormat());
        newModelOTHWebDTO.setSizeOTH(modelOTH.getSizeOTH());

        newModelOTHWebDTO.setFullPreview(imageService.getBaseSFimgWeb(modelOTH, true, 0.5f));

        return newModelOTHWebDTO;
    }

}
