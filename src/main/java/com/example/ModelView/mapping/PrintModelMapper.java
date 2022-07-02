package com.example.ModelView.mapping;


import com.example.ModelView.model.entities.web.PrintModelWebData;
import com.example.ModelView.model.rest.PrintModel;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.model.rest.PrintModelWeb;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {PrintModelOthMapper.class, PrintModelTagMapper.class, PrintModelZipMapper.class})
public abstract class PrintModelMapper {

    public abstract PrintModelPreview dataToApiPreview (PrintModelWebData source);

    public abstract List<PrintModelPreview> dataToApiPreview (List<PrintModelWebData> source);

    public abstract PrintModel dataToApi (PrintModelWebData source);

    public abstract PrintModelWeb dataToApiFull (PrintModelWebData source);

    public abstract List<PrintModelWeb> dataToApiFull (List<PrintModelWebData> source);

    @AfterMapping
    public void setPreview(PrintModelWebData source, @MappingTarget PrintModelPreview target){
        target.setCompressPreview(source.getPreviewModel().getPreviewOth());
    }

}
