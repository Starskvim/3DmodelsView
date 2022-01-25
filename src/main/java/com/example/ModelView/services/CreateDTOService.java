package com.example.ModelView.services;


import com.example.ModelView.dto.MapperDTO;
import com.example.ModelView.dto.PrintModelDTO;
import com.example.ModelView.entities.PrintModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateDTOService {
    private final MapperDTO mapperDTO;
    
    private ExecutorService executorService = Executors.newFixedThreadPool(8);

    private List<Future<PrintModelDTO>> futureList = new ArrayList<Future<PrintModelDTO>>();
    private List<PrintModelDTO> resultList = new ArrayList<PrintModelDTO>(40);


    public List<PrintModelDTO> createDTOlistThreads(Page<PrintModel> modelsPages) {

        if (!futureList.isEmpty()) {
            futureList.clear();
        }
        if (!resultList.isEmpty()) {
            resultList.clear();
        }
        for (PrintModel printModel : modelsPages.getContent()) {
            Future<PrintModelDTO> future = executorService.submit(new ImageWorkerThreadService(printModel, mapperDTO));
            futureList.add(future);
        }

        for (Future<PrintModelDTO> future : futureList) {
            try {
                PrintModelDTO result = future.get();
                resultList.add(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return this.resultList;
    }


    public List<PrintModelDTO> createDTOlistStream(Page<PrintModel> modelsPages) {

        if (!futureList.isEmpty()) {
            futureList.clear();
        }
        if (!resultList.isEmpty()) {
            resultList.clear();
        }

        List<PrintModelDTO> resultListStream = modelsPages
                .getContent()
                .parallelStream()
                .map(printModel -> mapperDTO.toPrintModelDTO(printModel))
                .collect(Collectors.toList());

        return resultListStream;
    }

//    public List<PrintModelDTO> createDTOlistStreamMapsStruct(Page<PrintModel> modelsPages) {
//
//        if (!futureList.isEmpty()) {
//            futureList.clear();
//        }
//        if (!resultList.isEmpty()) {
//            resultList.clear();
//        }
//
//        List<PrintModelDTO> resultListStream = modelsPages
//                .getContent()
//                .parallelStream()
//                .map(printModel -> )
//                .collect(Collectors.toList());
//
//        return resultListStream;
//    }

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