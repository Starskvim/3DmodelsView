package com.example.ModelView.mapping;

import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.rest.PrintModelOthWeb;
import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.model.entities.locale.PrintModelTagData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.services.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class MapperDto {

    private final ImageService imageService;

    public PrintModelWeb toPrintModelWebDTO (PrintModelData printModelData){

        PrintModelWeb newPrintModelWeb = new PrintModelWeb();
        newPrintModelWeb.setModelName(printModelData.getModelName());
        newPrintModelWeb.setModelPath(printModelData.getModelDirectory());
        newPrintModelWeb.setModelCategory(printModelData.getModelCategory());

        Set<PrintModelOthData> printModelOthDataSet = printModelData.getPrintModelOthDataSet();
        List<PrintModelTagData> tagsList = printModelData.getModelTagsObjData();

        Set<String> tagsNamesList = new HashSet<>();
        for (PrintModelTagData printModelTagData : tagsList){
            tagsNamesList.add(printModelTagData.getTag());
        }

        System.out.println("toPrintModelWebDTO size out tags list - " + tagsNamesList.size());

        ArrayList<PrintModelOthWeb> resiltList = new ArrayList<>();
        for(PrintModelOthData printModelOthData : printModelOthDataSet){
            resiltList.add(toModelOTHWebDTO(printModelOthData));
        }
        Double totalSize = 0d;
        for (PrintModelZipData printModelZipData : printModelData.getPrintModelZipDataSet()){
            totalSize += printModelZipData.getZipSize();
        }

        newPrintModelWeb.setModelTagsNames(tagsNamesList);
        newPrintModelWeb.setModelSize(totalSize);
        newPrintModelWeb.setModelOTHList(resiltList);

        return newPrintModelWeb;
    }

    public PrintModelOthWeb toModelOTHWebDTO (PrintModelOthData printModelOthData){

        PrintModelOthWeb newPrintModelOthWeb = new PrintModelOthWeb();
        newPrintModelOthWeb.setNameModelOTH(printModelOthData.getNameModelOth());
        newPrintModelOthWeb.setModelName(printModelOthData.getModelName());
        newPrintModelOthWeb.setFileClass(printModelOthData.getFileClass());
        newPrintModelOthWeb.setModelOTHFormat(printModelOthData.getOthFormat());
        newPrintModelOthWeb.setSizeOTH(printModelOthData.getOthSize());
        newPrintModelOthWeb.setPreviewOth(imageService.getBase64ImgWeb(printModelOthData, true, 0.5f));

        return newPrintModelOthWeb;
    }

}
