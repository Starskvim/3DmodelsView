package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.repositories.ModelRepositoryJPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreateObjService {

    private final ModelRepositoryJPA modelRepositoryJPA;
    private final FolderScanRepository folderScanRepository;
    private final EntitiesAttributeService entitiesAttributeService;
    private final CollectionsService collectionsService;

    private final JsProgressBarService jsProgressBarService;

    private int filesListSize = 0;
    private volatile int countDone = 0;


    CopyOnWriteArraySet<PrintModel> printModelsToSaveList;
    CopyOnWriteArraySet<ModelOTH> modelOTHList;
    CopyOnWriteArraySet<ModelZIP> modelZIPList;
    CopyOnWriteArrayList<String> zipFormatList;
    CopyOnWriteArraySet<String> printModelsToSaveNameStringSet;


    @PostConstruct
    private void postConstruct() {
        printModelsToSaveList = collectionsService.getPrintModelsToSaveList();
        modelOTHList = collectionsService.getModelOTHList();
        modelZIPList = collectionsService.getModelZIPList();
        zipFormatList = collectionsService.getZipFormatList();
        printModelsToSaveNameStringSet = collectionsService.getPrintModelsToSaveNameStringSet();
    }


    public void startCreateOBJService() throws IOException {

        Collection<File> filesList = folderScanRepository.startScanRepository(true);
        zipFormatList.add("zip");
        zipFormatList.add("7z");
        zipFormatList.add("rar");

        printModelsToSaveNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());

        filesListSize = filesList.size();

        JsProgressBarService.setTotalCount(filesListSize);

        filesList.parallelStream().forEach(file -> detectTypeCreate(file));



        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveList.size());

    }

    private void detectTypeCreate(File file) {

        if (collectionsService.checkPrintModelsNameStringSet(file.getParentFile().getName())) {
            checkAndCreateOBJ(file);
        } else {
            createPrintModelOBJ(file);
            checkAndCreateOBJ(file);
        }
        countDone += 1;
        JsProgressBarService.setCurrentCount(countDone);
        JsProgressBarService.setCurrentTask(countDone + "/" + filesListSize + " - create - " + file.getName());
        System.out.println(countDone + "/" + filesListSize + " - create - " + file.getName());

    }


    private void checkAndCreateOBJ(File file) {
        if (zipFormatList.contains(FilenameUtils.getExtension(file.getName()))) {
            createModelZIP(file);
        } else {
            createModelOTH(file);
        }
    }

    private void createPrintModelOBJ(File file) {
        String category = entitiesAttributeService.detectPrintModelCategory(file);
        ArrayList<String> modelTag = entitiesAttributeService.detectTag(file);
        PrintModel printModel = new PrintModel(file.getParentFile().getName(), file.getParent(), category, modelTag);
        printModelsToSaveNameStringSet.add(file.getParentFile().getName());
        printModelsToSaveList.add(printModel);
    }

    private void createModelOTH(File file) {
        Double size = entitiesAttributeService.getSizeFileToString(file);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size);
        modelOTHList.add(modelOTH);
        getModelListOTHRepositoryService(file, modelOTH);
    }

    private void createModelZIP(File file) {
        Double size = entitiesAttributeService.getSizeFileToString(file);
        String format = FilenameUtils.getExtension(file.getName());
        int ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPList.add(modelZIP);
        getModelListZIPService(file, modelZIP);
    }


    private void getModelListZIPService(File file, ModelZIP modelZip) {
        for (PrintModel printModel : printModelsToSaveList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelZIP(modelZip);
                break;
            }
        }
    }

    private void getModelListOTHRepositoryService(File file, ModelOTH modelOTH) {
        for (PrintModel printModel : printModelsToSaveList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelOTH(modelOTH);
                break;
            }
        }
    }


}
