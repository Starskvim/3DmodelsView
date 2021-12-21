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

    public PrintModelDTO toDTO(PrintModel printModel){
        Long id = printModel.getId();
        String modelName = printModel.getModelName();
        String modelDerictory = printModel.getModelDerictory();
        String modelCategory = printModel.getModelCategory();
        Collection<ModelZIP> modelZIPList = printModel.getModelZIPList();
        Collection<ModelOTH> modelOTHList = printModel.getModelOTHList();
        String compressPreview = imageService.getPreviewBaseSFimg(printModel, true);

        return new PrintModelDTO(id, modelName, modelDerictory, modelCategory, modelZIPList, modelOTHList, compressPreview);
    }

}
