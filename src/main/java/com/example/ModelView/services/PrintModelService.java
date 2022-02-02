package com.example.ModelView.services;

import com.example.ModelView.dto.ModelOTHDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.*;
import com.example.ModelView.repositories.jpa.ModelRepositoryJPA;
import com.example.ModelView.repositories.jpa.ModelRepositoryZIPJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrintModelService {
    private final FolderScanRepository folderScanRepository;
    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;

    public List<PrintModel> getAllModelListService(){
        return modelRepositoryJPA.findAll();
    }

    public Page<PrintModel> findAllModelByPageAndSpecsService(Specification<PrintModel> modelSpecification, Pageable pageable){
        return modelRepositoryJPA.findAll(modelSpecification, pageable);
    }

    public Page<ModelZIP> getAllZIPListByPageService(Pageable pageable){
        return modelRepositoryZIPJPA.findAll(pageable);
    }

    public void startFolderScanService () throws IOException {
        folderScanRepository.startScanRepository(true);
    }

    public PrintModel getById (Long id) {
        Optional<PrintModel> printModel = modelRepositoryJPA.findById(id);
        return printModel.orElse(null);
    }

    public void openFolderOrFile (String adress) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select," + adress);
    }

    public List<PrintModel> searchByModelNameService (String word, int page) {
        return modelRepositoryJPA.findAllBymodelNameLikeIgnoreCase(word, PageRequest.of(page, 50)).toList();
    }

    public List<Integer> preparePageIntService(int current, int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        int start = Math.max(current - 3, 0);
        int end = Math.min(totalPages, start + 9);
        pageNumbers.add(0);
        for (int i = start; i < end; i++) {
            if (i != 0 && i != totalPages - 1) {
                pageNumbers.add(i);
            }
        }
        pageNumbers.add(totalPages - 1);
        return pageNumbers;
    }

}
