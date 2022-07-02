package com.example.ModelView.mapping;

import com.example.ModelView.model.entities.web.PrintModelOthWebData;
import com.example.ModelView.model.rest.PrintModelOth;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PrintModelOthMapper {

    public abstract PrintModelOth dataToApi (PrintModelOthWebData source);

    public abstract List<PrintModelOth> dataToApi (List<PrintModelOthWebData> source);

}
