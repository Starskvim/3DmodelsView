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

@Service
@RequiredArgsConstructor
@Log4j2
public class CreateObjService {

    private final ModelRepositoryJPA modelRepositoryJPA;
    //private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    //private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;
    private final FolderScanRepository folderScanRepository;
    private final EntitiesAttributeService entitiesAttributeService;
    private final CollectionsService collectionsService;



    Collection<PrintModel> printModelsToSaveList;
    Collection<ModelOTH> modelOTHList;
    Collection<ModelZIP> modelZIPList;
    ArrayList<String> zipFormatList;
    HashSet<String> printModelsToSaveNameStringSet;


    @PostConstruct
    private void postConstruct(){
        printModelsToSaveList = collectionsService.getPrintModelsToSaveList();
        modelOTHList = collectionsService.getModelOTHList();
        modelZIPList = collectionsService.getModelZIPList();
        zipFormatList = collectionsService.getZipFormatList();
        printModelsToSaveNameStringSet = collectionsService.getPrintModelsToSaveNameStringSet();
    }



    public void startCreateOBJService() throws IOException {

        Collection<File> filesList = folderScanRepository.startScanRepository();
        zipFormatList.add("zip");
        zipFormatList.add("7z");
        zipFormatList.add("rar");

        printModelsToSaveNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());


        int filesListSize = filesList.size();
        int countDone = 0;


        for (File file : filesList) {
            if (collectionsService.checkPrintModelsNameStringSet(file.getParentFile().getName())) {
                checkAndCreateOBJ(file);
            } else {
                createPrintModelOBJ(file);
                checkAndCreateOBJ(file);
            }
            countDone += 1;
            System.out.println(countDone + "/" + filesListSize + " - " + file.getName());
        }


        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveList.size());

    }



    public void checkAndCreateOBJ(File file) {
        if (zipFormatList.contains(FilenameUtils.getExtension(file.getName()))) {
            createModelZIP(file);
        } else {
            createModelOTH(file);
        }
    }

    public void createPrintModelOBJ(File file) {
        String category = entitiesAttributeService.detectPrintModelCategory(file);
        PrintModel printModel = new PrintModel(file.getParentFile().getName(), file.getParent(), category);
        printModelsToSaveNameStringSet.add(file.getParentFile().getName());
        printModelsToSaveList.add(printModel);
    }

    public void createModelOTH(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getAbsolutePath(), format, size);
        modelOTHList.add(modelOTH);
        getModelListOTHRepositoryService(file, modelOTH);
    }

    public void createModelZIP(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        double ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPList.add(modelZIP);
        getModelListZIPService(file, modelZIP);
    }



    public void getModelListZIPService(File file, ModelZIP modelZip) {
        for (PrintModel printModel : printModelsToSaveList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelZIP(modelZip);
                break;
            }
        }
    }

    public void getModelListOTHRepositoryService(File file, ModelOTH modelOTH) {
        for (PrintModel printModel : printModelsToSaveList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelOTH(modelOTH);
                break;
            }
        }
    }


}
