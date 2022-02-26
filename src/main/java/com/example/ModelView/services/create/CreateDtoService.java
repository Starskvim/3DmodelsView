package com.example.ModelView.services.create;


import com.example.ModelView.dto.MapperAbstract;
import com.example.ModelView.dto.ModelOTHDto;
import com.example.ModelView.dto.PrintModelPreviewDto;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.services.image.ImageWorkerThreadService;
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
    private final MapperAbstract mapperAbstract;

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    private List<Future<PrintModelPreviewDto>> futureList = new ArrayList<>();
    private List<PrintModelPreviewDto> resultList = new ArrayList<>(40);

    public Collection<ModelOTHDto> prepareOTHListDTOService (Collection<ModelOTH> printModelOTHList){
        Collection<ModelOTHDto> resultListOTHDTO = new ArrayList<>(10);
        for (ModelOTH modelOTH : printModelOTHList) {
            resultListOTHDTO.add(mapperAbstract.toModelOTHDTO(modelOTH));
        }
        return resultListOTHDTO;
    }

    public List<PrintModelPreviewDto> createDTOListThreads(Page<PrintModel> modelsPages) {

        if (!futureList.isEmpty()) {
            futureList.clear();
        }
        if (!resultList.isEmpty()) {
            resultList.clear();
        }
        for (PrintModel printModel : modelsPages.getContent()) {
            Future<PrintModelPreviewDto> future = executorService.submit(new ImageWorkerThreadService(printModel, mapperAbstract));
            futureList.add(future);
        }

        for (Future<PrintModelPreviewDto> future : futureList) {
            try {
                PrintModelPreviewDto result = future.get();
                resultList.add(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return this.resultList;
    }

    public List<PrintModelPreviewDto> createDTOlistStream(Page<PrintModel> modelsPages) {

        if (!futureList.isEmpty()) {
            futureList.clear();
        }
        if (!resultList.isEmpty()) {
            resultList.clear();
        }

        List<PrintModelPreviewDto> resultListStream = modelsPages
                .getContent()
                .parallelStream()
                .map(printModel -> mapperAbstract.toPrintModelDTO(printModel))
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