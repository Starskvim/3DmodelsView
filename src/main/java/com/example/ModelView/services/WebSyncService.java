package com.example.ModelView.services;

import com.example.ModelView.entities.PrintModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebSyncService {

    private final PrintModelService printModelService;
    private final WebRestService webRestService;

    public void startSyncWeb (){

        List<String> listWebModels = Arrays.stream(webRestService.getWebModelList()).collect(Collectors.toList());

        ArrayList<String> listLocalModels = (ArrayList<String>) printModelService.getAllModelsName();

        listLocalModels.removeAll(listWebModels);

        List<PrintModel> printModelToPostList = printModelService.getModelsByNames(listLocalModels);

        if(!printModelToPostList.isEmpty()){
            Iterator<PrintModel> i = printModelToPostList.listIterator();
            while (i.hasNext()){ // TODO test
                PrintModel modelToPost = i.next();
                printModelService.postSyncModelOnWeb(modelToPost);
                i.remove();
            }
        }

    }
}
