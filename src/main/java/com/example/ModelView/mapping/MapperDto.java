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
    private final PrintModelZipMapper zipMapper;

    public PrintModelWeb toPrintModelWebDto(PrintModelData printModelData){

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

        ArrayList<PrintModelOthWeb> resultList = new ArrayList<>();
        for(PrintModelOthData printModelOthData : printModelOthDataSet){
            resultList.add(toModelOthWebDto(printModelOthData));
        }
        Double totalSize = 0d;
        for (PrintModelZipData printModelZipData : printModelData.getPrintModelZipDataSet()){
            totalSize += printModelZipData.getZipSize();
        }

        newPrintModelWeb.setModelTagsNames(tagsNamesList);
        newPrintModelWeb.setModelSize(totalSize);
        newPrintModelWeb.setModelOthList(resultList);
        newPrintModelWeb.setModelZips(zipMapper.dataToWeb(printModelData.getPrintModelZipDataSet()));

        return newPrintModelWeb;
    }

    private PrintModelOthWeb toModelOthWebDto(PrintModelOthData printModelOthData){

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
