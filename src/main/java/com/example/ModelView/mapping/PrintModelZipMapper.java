package com.example.ModelView.mapping;

import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.rest.PrintModelZip;
import com.example.ModelView.model.rest.PrintModelZipWeb;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public abstract class PrintModelZipMapper {

    public abstract PrintModelZip dataToApi(PrintModelZipData source);

    public abstract PrintModelZipWeb dataToWeb(PrintModelZipData source);

    public abstract Collection<PrintModelZipWeb> dataToWeb(Collection<PrintModelZipData> source);
}
