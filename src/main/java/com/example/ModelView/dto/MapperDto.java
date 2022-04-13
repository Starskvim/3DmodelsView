package com.example.ModelView.dto;

import com.example.ModelView.dto.web.PrintModelOTHWebDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.locale.ModelOTH;
import com.example.ModelView.entities.locale.ModelTag;
import com.example.ModelView.entities.locale.ModelZIP;
import com.example.ModelView.entities.locale.PrintModel;
import com.example.ModelView.services.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

        ArrayList<PrintModelOTHWebDTO> resiltList = new ArrayList<>();
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

    public PrintModelOTHWebDTO toModelOTHWebDTO (ModelOTH modelOTH){

        PrintModelOTHWebDTO newPrintModelOTHWebDTO = new PrintModelOTHWebDTO();
        newPrintModelOTHWebDTO.setNameModelOTH(modelOTH.getNameModelOTH());
        newPrintModelOTHWebDTO.setModelName(modelOTH.getModelName());
        newPrintModelOTHWebDTO.setFileClass(modelOTH.getFileClass());
        newPrintModelOTHWebDTO.setModelOTHFormat(modelOTH.getModelOTHFormat());
        newPrintModelOTHWebDTO.setSizeOTH(modelOTH.getSizeOTH());
        newPrintModelOTHWebDTO.setPreviewOth(imageService.getBaseSFimgWeb(modelOTH, true, 0.5f));

        return newPrintModelOTHWebDTO;
    }

}
