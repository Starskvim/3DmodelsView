package com.example.ModelView.mapping;


import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.rest.PrintModel;
import com.example.ModelView.model.rest.PrintModelOth;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.services.image.ImageService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.ModelView.utillity.CreateUtils.trimStringNameModel;

@Mapper
public abstract class OldPrintModelMapper {

    @Autowired
    protected ImageService imageService;

    public abstract PrintModel toPrintModelDto (PrintModelData printModelData);

    @Mapping(source = "printModelData", target = "modelName", qualifiedByName = "toTrimStringNameModel")
    @Mapping(source = "printModelData", target = "compressPreview", qualifiedByName = "toPreviewBase64Img")
    public abstract PrintModelPreview toPrintModelPreviewDTO(PrintModelData printModelData);

    @Mapping(source = "printModelOthData", target = "previewOth", qualifiedByName = "toPreviewBase64Img")
    public abstract PrintModelOth toModelOTHDTO(PrintModelOthData printModelOthData);

    @Named("toTrimStringNameModel")
    protected String toTrimStringNameModel(PrintModelData printModelData){
        return trimStringNameModel(printModelData.getModelName());
    }

    @Named("toPreviewBase64Img")
    protected String toPreviewBase64Img(PrintModelData printModelData){
        return imageService.getPreviewBase64Img(printModelData, true);
    }

    @Named("toPreviewBase64Img")
    protected String toFullBase64Img(PrintModelOthData printModelOthData){
        return imageService.getFullBase64Img(printModelOthData);
    }
}
