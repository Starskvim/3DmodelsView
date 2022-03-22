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

        List<String> listWebModels = webRestService.getWebModelList();

        for (String model: listWebModels){
            System.out.println(model + " - ");
        }

        ArrayList<String> listLocalModels = (ArrayList<String>) printModelService.getAllModelsName();

        System.out.println("listLocalModels size - " + listLocalModels.size());

        listLocalModels.removeAll(listWebModels);

        System.out.println("listLocalModels after size - " + listLocalModels.size());

        List<PrintModel> printModelToPostList = printModelService.getModelsByNames(listLocalModels);

        List<PrintModel> printModelToPostListTest = new ArrayList<>();

        printModelToPostList.stream().limit(5).forEach(printModelToPostListTest::add); // TODO test sync web size 5 model

        System.out.println("listLocalModelsTest size - " + printModelToPostListTest.size());

        if(!printModelToPostListTest.isEmpty()){
            Iterator<PrintModel> i = printModelToPostListTest.listIterator();
            while (i.hasNext()){ // TODO need test
                PrintModel modelToPost = i.next();
                printModelService.postSyncModelOnWeb(modelToPost);
                System.out.println("Post ready" + modelToPost.getModelName());
                i.remove();
            }
        }

    }
}
