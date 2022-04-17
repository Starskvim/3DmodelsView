package com.example.ModelView.services;

import com.example.ModelView.controllers.exceptions.ModelNotFoundException;
import com.example.ModelView.dto.MapperAbstract;
import com.example.ModelView.dto.MapperDto;
import com.example.ModelView.dto.PrintModelDto;
import com.example.ModelView.entities.locale.ModelZIP;
import com.example.ModelView.entities.locale.PrintModel;
import com.example.ModelView.repositories.*;
import com.example.ModelView.repositories.jpa.locale.ModelRepositoryJPA;
import com.example.ModelView.repositories.jpa.locale.ModelRepositoryTagsJPA;
import com.example.ModelView.repositories.jpa.locale.ModelRepositoryZIPJPA;
import com.example.ModelView.services.create.locale.CreateSyncObjService;
import com.example.ModelView.services.lokal.FolderSyncService;
import com.example.ModelView.services.lokal.SyncSerializeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrintModelService {
    private final FolderScanRepository folderScanRepository;
    private final FolderSyncService folderSyncService;
    private final SyncSerializeService syncSerializeService;
    private final CreateSyncObjService createSyncObjService;
    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    private final ModelRepositoryTagsJPA modelRepositoryTagsJPA;

    private final WebRestService webRestService;

    private final MapperDto mapperDto;
    private final MapperAbstract mapperAbstract;

    public List<PrintModel> getAllModelListService() {
        return modelRepositoryJPA.findAll();
    }

    public Collection<PrintModel> getSyncSerModelListService() {
        return syncSerializeService.getModelForSer();
    }

    public Page<PrintModel> findAllModelByPageAndSpecsService(Specification<PrintModel> modelSpecification, Pageable pageable) {
        return modelRepositoryJPA.findAll(modelSpecification, pageable);
    }

    public Page<ModelZIP> getAllZIPListByPageService(Pageable pageable) {
        return modelRepositoryZIPJPA.findAll(pageable);
    }

    public void startFolderScanService() throws IOException {
        folderScanRepository.startScanRepository(true);
    }

    public PrintModel getById(Long id) {
        Optional<PrintModel> printModel = modelRepositoryJPA.findById(id);
        return printModel.orElseThrow(() -> new ModelNotFoundException(id));
    }

    public List<PrintModel> getModelsByNames(ArrayList<String> modelsNames){
        return modelRepositoryJPA.findAllByModelNameIn(modelsNames);
    }

    @Transactional
    public void deleteModelById(Long id) {
        modelRepositoryJPA.deleteById(id);
    }

    public void openFolderOrFile(String adress) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select," + adress);
    }

    public List<String> getAllTagsName(){
        return modelRepositoryTagsJPA.getAllNameTags();
    }

    public Page<PrintModel> getAllModelByTagService(String tag, Pageable pageable){
        return modelRepositoryJPA.findAllByModelTagsObj_TagContaining(tag, pageable);
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

    public PrintModelDto createDto(PrintModel printModel) {
        return mapperAbstract.toPrintModelDto(printModel);
    }

    public void postModelOnWeb(Long id) {
        PrintModel printModel = getById(id);
        System.out.println("post get - " + printModel.getModelName());
        webRestService.createPostModel(mapperDto.toPrintModelWebDTO(printModel));
    }

    public void postSyncModelOnWeb(PrintModel printModel) {
        System.out.println("postSync get - " + printModel.getModelName());
        webRestService.createPostModel(mapperDto.toPrintModelWebDTO(printModel));
    }

    public List<String> getAllModelsName() {
        return modelRepositoryJPA.getAllNameModel();
    }

    public void startSyncFolderService() {
        folderSyncService.startSyncFolderService();
    }

    public void startSyncObjService() {
        try {
            createSyncObjService.startSyncOBJRepository();
        } catch (IOException a) {
            System.out.println("IOException");
        }
    }

}
