package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.repositories.ModelRepositoryJPA;
import com.example.ModelView.repositories.ModelRepositoryOTHJPA;
import com.example.ModelView.repositories.ModelRepositoryZIPJPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.swing.*;
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
public class CreateSyncObjService {

    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;

    private final FolderScanRepository folderScanRepository;
    private final EntitiesAttributeService entitiesAttributeService;
    private final CollectionsService collectionsService;

    private final JProgressBarService jProgressBarService;


    CopyOnWriteArraySet<PrintModel> printModelsToSaveList;
    CopyOnWriteArraySet<ModelOTH> modelOTHList;
    CopyOnWriteArraySet<ModelZIP> modelZIPList;
    CopyOnWriteArrayList<String> zipFormatList;
    CopyOnWriteArraySet<String> printModelsToSaveNameStringSet;



    HashSet<String> printModelsSavedNameStringSet;
    HashSet<String> printModelsSavedFilesNameStringSet;

    HashSet<String> printModelsSavedFilesAdressStringSet;


    @PostConstruct
    private void postConstruct() {
        printModelsToSaveList = collectionsService.getPrintModelsToSaveList();
        modelOTHList = collectionsService.getModelOTHList();
        modelZIPList = collectionsService.getModelZIPList();
        zipFormatList = collectionsService.getZipFormatList();
        printModelsToSaveNameStringSet = collectionsService.getPrintModelsToSaveNameStringSet();
        printModelsSavedNameStringSet = collectionsService.getPrintModelsSavedNameStringSet();

        printModelsSavedFilesNameStringSet = collectionsService.getPrintModelsSavedFilesNameStringSet();

        printModelsSavedFilesAdressStringSet = collectionsService.getPrintModelsSavedFilesAdressStringSet();
    }


    @Transactional
    public void startSyncOBJRepository() throws IOException {

        Collection<File> filesList = folderScanRepository.startScanRepository();
        zipFormatList.add("zip");
        zipFormatList.add("7z");
        zipFormatList.add("rar");

        printModelsToSaveNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());

        //?
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryOTHJPA.getAllnameModelOTH());
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryZIPJPA.getAllnameModelZIP());

        printModelsSavedFilesAdressStringSet.addAll(modelRepositoryOTHJPA.getAllmodelOTHAdress());
        printModelsSavedFilesAdressStringSet.addAll(modelRepositoryZIPJPA.getAllmodelZIPAdress());


        printModelsSavedNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());

        int filesListSize = filesList.size();
        int countDone = 0;

        JProgressBarService newProgressBar = new JProgressBarService("SyncService", Math.abs(filesListSize - printModelsSavedFilesAdressStringSet.size()));


        for (File file : filesList) {

            if (collectionsService.checkPrintModelsFilesSavedNameStringSet(file.getName())) {
                if (collectionsService.checkPrintModelsNameStringSet(file.getParentFile().getName())) {
                    checkAndCreateOBJ(file);
                } else {
                    createPrintModelOBJ(file);
                    checkAndCreateOBJ(file);
                }
                countDone += 1;
                newProgressBar.updateBar(countDone);
                System.out.println(countDone + "/" + filesListSize + " - sync - " + file.getName());
            }
        }

        collectionsService.saveAllListToJpaRepository();

        // ? Очередность, вызов не существующих листов ?
        checkAndDeleteOBJinListOTHandZIP(filesList);
        deleteModelsWhereDeletedFiles(filesList);

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
        String size = entitiesAttributeService.getSizeFileToString(file);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size);
        modelOTHList.add(modelOTH);
        String nameModel = file.getParentFile().getName();
        if (printModelsSavedNameStringSet.contains(nameModel)) {
            addInSavedModelOTHList(modelOTH, nameModel);
        } else {
            addToSaveModelListOTH(file, modelOTH);
        }
    }

    public void createModelZIP(File file) {
        String size = entitiesAttributeService.getSizeFileToString(file);
        String format = FilenameUtils.getExtension(file.getName());

        int ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());

        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPList.add(modelZIP);
        String nameModel = file.getParentFile().getName();
        if (printModelsSavedNameStringSet.contains(nameModel)) {
            addInSavedModelZIPList(modelZIP, nameModel);
        } else {
            addToSaveModelListZIP(file, modelZIP);
        }
    }

    public void addInSavedModelZIPList(ModelZIP modelZIP, String nameModel) {
        PrintModel printModel = modelRepositoryJPA.getByModelName(nameModel);
        System.out.println("------addInSavedModelOTHList - " + nameModel + " ---to--- " + printModel.getModelName());
        printModel.addModelZIP(modelZIP);
        printModelsToSaveList.add(printModel);
    }

    public void addInSavedModelOTHList(ModelOTH modelOTH, String nameModel) {
        PrintModel printModel = modelRepositoryJPA.getByModelName(nameModel);
        System.out.println("------addInSavedModelOTHList - " + nameModel + " -to- " + printModel.getModelName());
        printModel.addModelOTH(modelOTH);
        printModelsToSaveList.add(printModel);
    }


    public void addToSaveModelListZIP(File file, ModelZIP modelZip) {
        for (PrintModel printModel : printModelsToSaveList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelZIP(modelZip);
                break;
            }
        }
    }

    public void addToSaveModelListOTH(File file, ModelOTH modelOTH) {
        for (PrintModel printModel : printModelsToSaveList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelOTH(modelOTH);
                break;
            }
        }
    }

    public void deleteModelsWhereDeletedFiles(Collection<File> files) {
        HashSet<String> deleteModelSet = printModelsSavedNameStringSet;

        // mb stream
        for (File file : files) {
            deleteModelSet.remove(file.getParentFile().getName());
        }
        System.out.println("------deleteModelsWhereDeletedFiles - " + deleteModelSet.size());
        modelRepositoryJPA.deleteAllByModelNameIn(deleteModelSet);
    }

    public void checkAndDeleteOBJinListOTHandZIP(Collection<File> files) {

        Collection<ModelOTH> toDeleteOTHList = new ArrayList<>();
        Collection<ModelZIP> toDeleteZIPList = new ArrayList<>();

        for (File file : files) {
            printModelsSavedFilesAdressStringSet.remove(file.getAbsolutePath());
        }

        if (!printModelsSavedFilesAdressStringSet.isEmpty()) {
            for (String fileAbsPath : printModelsSavedFilesAdressStringSet) {
                if (zipFormatList.contains(FilenameUtils.getExtension(fileAbsPath))) {


                    try {
                        ModelZIP modelZIP = modelRepositoryZIPJPA.getModelZIPByModelZIPAdress(fileAbsPath);
                        PrintModel printModel = modelRepositoryJPA.getByModelName(modelZIP.getModelName());
                        Collection<ModelZIP> modelZIPList = printModel.getModelZIPList();
                        modelZIPList.remove(modelZIP);
                        toDeleteZIPList.add(modelZIP);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }



                } else {

                    try {
                        ModelOTH modelOTH = modelRepositoryOTHJPA.getModelOTHByModelOTHAdress(fileAbsPath);
                        PrintModel printModel = modelRepositoryJPA.getByModelName(modelOTH.getModelName());
                        Collection<ModelOTH> modelOTHList = printModel.getModelOTHList();
                        modelOTHList.remove(modelOTH);
                        toDeleteOTHList.add(modelOTH);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }


                }
            }
            modelRepositoryOTHJPA.deleteAll(toDeleteOTHList);
            modelRepositoryZIPJPA.deleteAll(toDeleteZIPList);
        }
    }


}
