package com.example.ModelView.services.image;

import com.example.ModelView.mapping.OldPrintModelMapper;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.model.entities.locale.PrintModelData;
import lombok.*;
import java.util.concurrent.Callable;


@RequiredArgsConstructor
@Setter
@Getter
public class ImageTask implements Callable<PrintModelPreview> {
    private PrintModelData printModelData;

    private OldPrintModelMapper oldPrintModelMapper;

    private PrintModelPreview printModelPreview;

    public ImageTask(PrintModelData printModelData, OldPrintModelMapper oldPrintModelMapper) {
        this.printModelData = printModelData;
        this.oldPrintModelMapper = oldPrintModelMapper;
    }

    @Override
    public PrintModelPreview call(){
        return oldPrintModelMapper.toPrintModelPreviewDTO(printModelData);
    }
}
