package com.example.ModelView.services;

import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.persistance.FolderScanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
                .limit(500)
                .forEach(this::prepareAndPostDto);


    }

    private Boolean checkContain(File file) {
        return !listWebModels.contains(file.getName().replace(".json", ""));
    }

    private void prepareAndPostDto(File file) {
        printModelService.postSyncModelOnWeb(getDtoToPost(file));
    }

    private PrintModelWeb getDtoToPost(File file) {
        try {
            return objectMapper.readValue(file, PrintModelWeb.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
