package com.example.ModelView.dto;


import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.PrintModel;
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

    @Mapping(target = "compressPreview", expression = "java(imageService.getPreviewBaseSFimg(printModel, true))")
    public abstract PrintModelPreviewDto toPrintModelDTO (PrintModel printModel);

    @Mapping(target = "fullPreview", expression = "java(imageService.getFullBaseSFimg(modelOTH))")
    public abstract ModelOTHDto toModelOTHDTO(ModelOTH modelOTH);

}
