package com.example.ModelView.services;

import com.example.ModelView.dto.MapperDTO;
import com.example.ModelView.dto.PrintModelDTO;
import com.example.ModelView.entities.PrintModel;
import lombok.*;
import java.util.concurrent.Callable;


@RequiredArgsConstructor
@Setter
@Getter
public class ImageWorkerThreadService implements Callable<PrintModelDTO> {
    private PrintModel printModel;

    private MapperDTO mapperDTO;

    private PrintModelDTO printModelDTO;

    public ImageWorkerThreadService(PrintModel printModel, MapperDTO mapperDTO) {
        this.printModel = printModel;
        this.mapperDTO = mapperDTO;
    }

    @Override
    public PrintModelDTO call(){
        return mapperDTO.toPrintModelDTO(printModel);
    }
}
