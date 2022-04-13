package com.example.ModelView.dto;


import com.example.ModelView.entities.locale.ModelOTH;
import com.example.ModelView.entities.locale.PrintModel;
import com.example.ModelView.services.create.EntitiesAttributeService;
import com.example.ModelView.services.image.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class MapperAbstract {

    @Autowired
    protected ImageService imageService;
    @Autowired
    protected EntitiesAttributeService entitiesAttributeService;

    public abstract PrintModelDto toPrintModelDto (PrintModel printModel);

    @Mapping(target = "modelName", expression = "java(entitiesAttributeService.trimStringNameModel(printModel.getModelName()))")
    @Mapping(target = "compressPreview", expression = "java(imageService.getPreviewBaseSFimg(printModel, true))")
    public abstract PrintModelPreviewDto toPrintModelPreviewDTO(PrintModel printModel);

    @Mapping(target = "fullPreview", expression = "java(imageService.getFullBaseSFimg(modelOTH))")
    public abstract ModelOTHDto toModelOTHDTO(ModelOTH modelOTH);

}
