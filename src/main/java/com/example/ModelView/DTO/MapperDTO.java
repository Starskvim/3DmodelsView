package com.example.ModelView.DTO;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

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

}
