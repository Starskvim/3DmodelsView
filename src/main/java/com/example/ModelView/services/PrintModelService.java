package com.example.ModelView.services;

import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.repositories.ModelRepositoryJPA;
import com.example.ModelView.repositories.ModelRepositoryOTHJPA;
import com.example.ModelView.repositories.ModelRepositoryZIPJPA;
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
        //return modelRepositoryJPA.findAll(PageRequest.of(page, 50)).toList();

        return modelRepositoryZIPJPA.findAll(PageRequest.of(page, 250)).toList();

    }


    public void startFolderScanService () throws IOException {
        folderScanRepository.startScanRepository();
    }

    public void startFolderCreateService () throws IOException {
        folderScanRepository.startCreateOBJRepository();
    }

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
