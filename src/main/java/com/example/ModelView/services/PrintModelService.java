package com.example.ModelView.services;

import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintModelService {
    private final FolderScanRepository folderScanRepository;
    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;



    public List<PrintModel> getAllModelListService(){
        return modelRepositoryJPA.findAll();
    }

    public List<PrintModel> getAllModelListByPageService(int page){
        return modelRepositoryJPA.findAll(PageRequest.of(page, 60)).toList();
    }

    public List<ModelZIP> getAllZIPListByPageService(int page){

        return modelRepositoryZIPJPA.findAll(PageRequest.of(page, 250)).toList();

    }


    public void startFolderScanService () throws IOException {
        folderScanRepository.startScanRepository();
    }

//    public void startFolderCreateService () throws IOException {
//        folderScanRepository.startCreateOBJRepository();
//    }
//
//    public void startSyncService() throws IOException {
//        folderSyncRepository.startSyncOBJRepository();
//    }

    public PrintModel getById (Long id) {
        return modelRepositoryJPA.findById(id).orElse(null);
    }

    public void openFolderOrFile (String adress) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select," + adress);
    }

    public List<PrintModel> searchByModelNameService (String word, int page) {
        return modelRepositoryJPA.findAllBymodelNameContains(word, PageRequest.of(page, 50)).toList();
    }

}
