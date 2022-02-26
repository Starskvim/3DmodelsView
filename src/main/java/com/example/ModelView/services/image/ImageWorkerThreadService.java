package com.example.ModelView.services.image;

import com.example.ModelView.dto.MapperAbstract;
import com.example.ModelView.dto.PrintModelPreviewDto;
import com.example.ModelView.entities.PrintModel;
import lombok.*;
import java.util.concurrent.Callable;


@RequiredArgsConstructor
@Setter
@Getter
public class ImageWorkerThreadService implements Callable<PrintModelPreviewDto> {
    private PrintModel printModel;

    private MapperAbstract mapperAbstract;

    private PrintModelPreviewDto printModelPreviewDTO;

    public ImageWorkerThreadService(PrintModel printModel, MapperAbstract mapperAbstract) {
        this.printModel = printModel;
        this.mapperAbstract = mapperAbstract;
    }

    @Override
    public PrintModelPreviewDto call(){
        return mapperAbstract.toPrintModelDTO(printModel);
    }
}
