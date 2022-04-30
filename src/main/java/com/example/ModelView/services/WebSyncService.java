package com.example.ModelView.services;

import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.locale.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.services.lokal.SerializeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
@RequiredArgsConstructor
public class WebSyncService {

    private final PrintModelService printModelService;
    private final WebRestService webRestService;
    private final FolderScanRepository folderScanRepository;
    private final ObjectMapper objectMapper;

    List<String> listWebModels;
    private List<String> listLocalModelsToPost;

    public void startSyncWeb() {

        listWebModels = webRestService.getWebModelList();

//        for (String model: listWebModels){
//            System.out.println(model + " - ");
//        }
//        ArrayList<String> listLocalModels = (ArrayList<String>) printModelService.getAllModelsName();

        Collection<File> inputSerFiles = folderScanRepository.startScanRepository(false);
        listLocalModelsToPost = inputSerFiles.stream()
                .map(File::getName)
                .map(name -> name.replace(".json", ""))
                .collect(Collectors.toList());




        System.out.println("listLocalModels size - " + listLocalModelsToPost.size());

        listLocalModelsToPost.stream().limit(5).forEach(System.out::println);
        listLocalModelsToPost.removeAll(listWebModels);
        System.out.println("--------------------");
        listLocalModelsToPost.stream().limit(5).forEach(System.out::println);

        System.out.println("listLocalModels after size - " + listLocalModelsToPost.size());

        inputSerFiles.stream()
                .filter(this::checkContain)
                .limit(5)
                .forEach(this::prepareAndPostDto);


    }

    private Boolean checkContain(File file) {
        return !listWebModels.contains(file.getName().replace(".json", ""));
    }

    private void prepareAndPostDto(File file) {
        printModelService.postSyncModelOnWeb(getDtoToPost(file));
    }

    private PrintModelWebDTO getDtoToPost(File file) {
        try {
            return objectMapper.readValue(file, PrintModelWebDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
