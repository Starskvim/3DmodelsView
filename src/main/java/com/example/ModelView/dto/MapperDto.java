package com.example.ModelView.dto;

import com.example.ModelView.dto.web.ModelOTHWebDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelTag;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.services.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@AllArgsConstructor
public class MapperDto {

    private final ImageService imageService;

    public PrintModelWebDTO toPrintModelWebDTO (PrintModel printModel){

        PrintModelWebDTO newPrintModelWebDTO = new PrintModelWebDTO();
        newPrintModelWebDTO.setModelName(printModel.getModelName());
        newPrintModelWebDTO.setModelPath(printModel.getModelDerictory());
        newPrintModelWebDTO.setModelCategory(printModel.getModelCategory());

        Set<ModelOTH> modelOTHSet = printModel.getModelOTHSet();
        List<ModelTag> tagsList = printModel.getModelTagsObj();

        Set<String> tagsNamesList = new HashSet<>();
        for (ModelTag modelTag: tagsList){
            tagsNamesList.add(modelTag.getTag());
        }

        System.out.println("toPrintModelWebDTO size out tags list - " + tagsNamesList.size());

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
