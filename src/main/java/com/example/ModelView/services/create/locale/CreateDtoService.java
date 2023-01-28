package com.example.ModelView.services.create.locale;


import com.example.ModelView.mapping.OldPrintModelMapper;
import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.rest.PrintModelOth;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.services.image.ImageTask;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateDtoService {
    private final OldPrintModelMapper oldPrintModelMapper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    private List<Future<PrintModelPreview>> futureList = new ArrayList<>();
    private List<PrintModelPreview> resultList = new ArrayList<>(40);

    public Collection<PrintModelOth> prepareOTHListDTOService (Collection<PrintModelOthData> printPrintModelOthDataList){
        Collection<PrintModelOth> resultListOTHDTO = new ArrayList<>(10);
        for (PrintModelOthData printModelOthData : printPrintModelOthDataList) {
            resultListOTHDTO.add(oldPrintModelMapper.toModelOTHDTO(printModelOthData));
        }
        return resultListOTHDTO;
    }

    public List<PrintModelPreview> createDTOListThreads(Page<PrintModelData> modelsPages) {

        if (!futureList.isEmpty()) {
            futureList.clear();
        }
        if (!resultList.isEmpty()) {
            resultList.clear();
        }
        for (PrintModelData printModelData : modelsPages.getContent()) {
            Future<PrintModelPreview> future = executorService.submit(new ImageTask(printModelData, oldPrintModelMapper));
            futureList.add(future);
        }

        for (Future<PrintModelPreview> future : futureList) {
            try {
                PrintModelPreview result = future.get();
                resultList.add(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return this.resultList;
    }

    public List<PrintModelPreview> createDTOlistStream(Page<PrintModelData> modelsPages) {

        if (!futureList.isEmpty()) {
            futureList.clear();
        }
        if (!resultList.isEmpty()) {
            resultList.clear();
        }

        List<PrintModelPreview> resultListStream = modelsPages
                .getContent()
                .parallelStream()
                .map(oldPrintModelMapper::toPrintModelPreviewDTO)
                .collect(Collectors.toList());

        return resultListStream;
    }

}


//Stream
// Create page 0 Time 1489
// Create page 1 Time 2278
// Create page 2 Time 1217
// Create page 3 Time 870
// Create page 4 Time 1347
// Create page 5 Time 1321
// Create page 6 Time 1111
//Tread (8)
// Create page 0 Time 1340
// Create page 1 Time 2410
// Create page 2 Time 1019
// Create page 3 Time 837
// Create page 4 Time 1237
// Create page 5 Time 1766
// Create page 6 Time 914