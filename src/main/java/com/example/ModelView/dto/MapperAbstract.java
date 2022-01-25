package com.example.ModelView.dto;


//import com.example.ModelView.entities.ModelOTH;
//import com.example.ModelView.entities.PrintModel;
//import com.example.ModelView.services.ImageService;
//import org.mapstruct.AfterMapping;
//import org.mapstruct.Mapper;
//import org.mapstruct.MappingTarget;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Mapper
//abstract class MapperAbstract {
//    private final ImageService imageService;
//
//    @Autowired
//    protected MapperAbstract(ImageService imageService) {
//        this.imageService = imageService;
//    }
//
//    @AfterMapping
//    protected void addPreviewToPrintModel(@MappingTarget PrintModelDTO printModelDTO, PrintModel printModel) {
//        printModelDTO.setCompressPreview(imageService.getPreviewBaseSFimg(printModel, true));
//    }
//
//    public abstract PrintModelDTO toPrintModelDTOMap (PrintModel printModel);
//
//
//    @AfterMapping
//    protected void addPreviewToModelOTH(@MappingTarget ModelOTHDTO modelOTHDTO, ModelOTH modelOTH) {
//        modelOTHDTO.setFullPreview(imageService.getFullBaseSFimg(modelOTH));
//    }
//
//    public abstract ModelOTHDTO toModelOTHDTODTOMap (ModelOTH modelOTH);
//
//
//}
