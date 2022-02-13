package com.example.ModelView.dto;

import com.example.ModelView.dto.web.ModelOTHWebDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelTag;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.services.create.EntitiesAttributeService;
import com.example.ModelView.services.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class MapperDTO {

    private final ImageService imageService;

    public PrintModelDTO toPrintModelDTO(PrintModel printModel){
        Long id = printModel.getId();
        String modelNameOld = StringUtils.trimLeadingCharacter(printModel.getModelName(), '+'); // TODO old name
        String modelName = StringUtils.trimTrailingWhitespace(modelNameOld);
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
        newPrintModelWebDTO.setModelPath(printModel.getModelDerictory());
        newPrintModelWebDTO.setModelCategory(printModel.getModelCategory());

        Set<ModelOTH> modelOTHSet = printModel.getModelOTHSet();
        List<ModelTag> tagsList = printModel.getModelTagsObj();

        List<String> tagsNamesList = new ArrayList<>();
        for (ModelTag modelTag: tagsList){
            tagsNamesList.add(modelTag.getTag());
        }
        ArrayList<ModelOTHWebDTO> resiltList = new ArrayList<>();
        for(ModelOTH modelOTH : modelOTHSet){
            resiltList.add(toModelOTHWebDTO(modelOTH));
        }
        Double totalSize = 0d;
        for (ModelZIP modelZIP: printModel.getModelZIPSet()){
            totalSize += modelZIP.getSizeZIP();
        }

        newPrintModelWebDTO.setModelTagsNames(tagsNamesList);
        newPrintModelWebDTO.setModelSize(totalSize);
        newPrintModelWebDTO.setModelOTHList(resiltList);

        return newPrintModelWebDTO;
    }

    public ModelOTHWebDTO toModelOTHWebDTO (ModelOTH modelOTH){

        ModelOTHWebDTO newModelOTHWebDTO = new ModelOTHWebDTO();
        newModelOTHWebDTO.setNameModelOTH(modelOTH.getNameModelOTH());
        newModelOTHWebDTO.setModelName(modelOTH.getModelName());
        newModelOTHWebDTO.setFileClass(modelOTH.getFileClass());
        newModelOTHWebDTO.setModelOTHFormat(modelOTH.getModelOTHFormat());
        newModelOTHWebDTO.setSizeOTH(modelOTH.getSizeOTH());
        newModelOTHWebDTO.setPreviewOth(imageService.getBaseSFimgWeb(modelOTH, true, 0.5f));

        return newModelOTHWebDTO;
    }

}
