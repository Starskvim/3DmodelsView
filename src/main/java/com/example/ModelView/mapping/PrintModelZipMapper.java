package com.example.ModelView.mapping;

import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.rest.PrintModelZip;
import org.mapstruct.Mapper;

@Mapper
public abstract class PrintModelZipMapper {

    public abstract PrintModelZip dataToApi (PrintModelZipData source);

}
