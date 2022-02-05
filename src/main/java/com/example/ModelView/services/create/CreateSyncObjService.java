package com.example.ModelView.services.create;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.repositories.jpa.ModelRepositoryJPA;
import com.example.ModelView.repositories.jpa.ModelRepositoryOTHJPA;
import com.example.ModelView.repositories.jpa.ModelRepositoryZIPJPA;
//import com.example.ModelView.services.lokal.JProgressBarService;
import com.example.ModelView.services.JsProgressBarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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

//    private final JProgressBarService jProgressBarService;
    private final JsProgressBarService jsProgressBarService;

    CopyOnWriteArraySet<PrintModel> printModelsToSaveSet;
    CopyOnWriteArraySet<ModelOTH> modelOTHToSaveSet;
    CopyOnWriteArraySet<ModelZIP> modelZIPToSaveSet;
    CopyOnWriteArrayList<String> zipFormatToSaveSet;
    CopyOnWriteArraySet<String> printModelsToSaveNameStringSet;

    HashSet<String> printModelsSavedNameStringSet;
    HashSet<String> printModelsSavedFilesNameStringSet;
    HashSet<String> printModelsSavedFilesAdressStringSet;

    @PostConstruct
    private void postConstruct() {
        printModelsToSaveSet = collectionsService.getPrintModelsToSaveList();
        modelOTHToSaveSet = collectionsService.getModelOTHList();
        modelZIPToSaveSet = collectionsService.getModelZIPList();
        zipFormatToSaveSet = collectionsService.getZipFormatList();
        printModelsToSaveNameStringSet = collectionsService.getPrintModelsToSaveNameStringSet();
        printModelsSavedNameStringSet = collectionsService.getPrintModelsSavedNameStringSet();
        printModelsSavedFilesNameStringSet = collectionsService.getPrintModelsSavedFilesNameStringSet();
        printModelsSavedFilesAdressStringSet = collectionsService.getPrintModelsSavedFilesAdressStringSet();
    }

    @Transactional
    public void startSyncOBJRepository() throws IOException {

        Collection<File> filesList = folderScanRepository.startScanRepository(true);
        zipFormatToSaveSet.add("zip");
        zipFormatToSaveSet.add("7z");
        zipFormatToSaveSet.add("rar");

        printModelsToSaveNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());
        //TODO ?
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryOTHJPA.getAllnameModelOTH());
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryZIPJPA.getAllnameModelZIP());
        printModelsSavedFilesAdressStringSet.addAll(modelRepositoryOTHJPA.getAllmodelOTHAdress());
        printModelsSavedFilesAdressStringSet.addAll(modelRepositoryZIPJPA.getAllmodelZIPAdress());
        printModelsSavedNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());

        int filesListSize = filesList.size();
        int taskSize = Math.abs(filesListSize - printModelsSavedFilesAdressStringSet.size());
        int countDone = 0;

        //JProgressBarService newProgressBar = new JProgressBarService("SyncService", taskSize); windows bar
        JsProgressBarService.setTotalCount(taskSize);
        entitiesAttributeService.prepareDetectTags(); // Tags

        for (File file : filesList) {
            if (collectionsService.checkPrintModelsFilesSavedNameStringSet(file.getName())) {
                if (collectionsService.checkPrintModelsNameStringSet(file.getParentFile().getName())) {
                    checkAndCreateOBJ(file);
                } else {
                    createPrintModelOBJ(file);
                    checkAndCreateOBJ(file);
                }
                countDone += 1;
                //newProgressBar.updateBar(countDone); windows bar
                JsProgressBarService.setCurrentCount(countDone);
                JsProgressBarService.setCurrentTask(countDone + "/" + filesListSize + " - sync - " + file.getName());
                System.out.println(countDone + "/" + filesListSize + " - sync - " + file.getName());
            }
        }

        printModelsToSaveSet.stream().forEach(model -> entitiesAttributeService.detectCreateObjTag(model.getModelDerictory())); // Tags
        printModelsToSaveSet.stream().forEach(model -> entitiesAttributeService.assignTags(model)); // Tags

        collectionsService.saveAllListToJpaRepository();

        //TODO ? Очередность
        checkAndDeleteOBJinListOTHandZIP(filesList);
        deleteModelsWhereDeletedFiles(filesList);

        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveSet.size());

    }

    public void checkAndCreateOBJ(File file) {

        if (zipFormatToSaveSet.contains(FilenameUtils.getExtension(file.getName()))) {
            createModelZIP(file);
        } else {
            createModelOTH(file);
        }
    }

    public void createPrintModelOBJ(File file) {
        String category = entitiesAttributeService.detectPrintModelCategory(file);

        PrintModel printModel = new PrintModel(file.getParentFile().getName(), file.getParent(), category);
        printModelsToSaveNameStringSet.add(file.getParentFile().getName());
        printModelsToSaveSet.add(printModel);
    }

    public void createModelOTH(File file) {
        Double size = entitiesAttributeService.getSizeFileToDouble(file);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size);
        modelOTHToSaveSet.add(modelOTH);
        String nameModel = file.getParentFile().getName();
        if (printModelsSavedNameStringSet.contains(nameModel)) {
            addInSavedModelOTHList(modelOTH, nameModel);
        } else {
            addToSaveModelListOTH(file, modelOTH);
        }
    }

    public void createModelZIP(File file) {
        Double size = entitiesAttributeService.getSizeFileToDouble(file);
        String format = FilenameUtils.getExtension(file.getName());
        int ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPToSaveSet.add(modelZIP);
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

        Set<ModelZIP> modelZIPSet = printModel.getModelZIPSet();
        modelZIPSet.add(modelZIP);



        printModelsToSaveSet.add(printModel);
    }

    public void addInSavedModelOTHList(ModelOTH modelOTH, String nameModel) {
        PrintModel printModel = modelRepositoryJPA.getByModelName(nameModel);
        System.out.println("------addInSavedModelOTHList - " + nameModel + " -to- " + printModel.getModelName());


        Set<ModelOTH> modelOTHSet = printModel.getModelOTHSet();
        modelOTHSet.add(modelOTH);


        printModelsToSaveSet.add(printModel);
    }

    public void addToSaveModelListZIP(File file, ModelZIP modelZip) {
        for (PrintModel printModel : printModelsToSaveSet) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {

                Set<ModelZIP> modelZIPSet = printModel.getModelZIPSet();
                modelZIPSet.add(modelZip);


                break;
            }
        }
    }

    public void addToSaveModelListOTH(File file, ModelOTH modelOTH) {
        for (PrintModel printModel : printModelsToSaveSet) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                Set<ModelOTH> modelOTHSet = printModel.getModelOTHSet();
                modelOTHSet.add(modelOTH);
                break;
            }
        }
    }

    public void deleteModelsWhereDeletedFiles(Collection<File> files) {
        HashSet<String> deleteModelSet = printModelsSavedNameStringSet;
        // TODO mb stream
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
                if (zipFormatToSaveSet.contains(FilenameUtils.getExtension(fileAbsPath))) {
                    try {
                        ModelZIP modelZIP = modelRepositoryZIPJPA.getModelZIPByModelZIPAdress(fileAbsPath);
                        PrintModel printModel = modelRepositoryJPA.getByModelName(modelZIP.getModelName());
                        Collection<ModelZIP> modelZIPList = printModel.getModelZIPSet();
                        modelZIPList.remove(modelZIP);
                        toDeleteZIPList.add(modelZIP);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        ModelOTH modelOTH = modelRepositoryOTHJPA.getModelOTHByModelOTHAdress(fileAbsPath);
                        PrintModel printModel = modelRepositoryJPA.getByModelName(modelOTH.getModelName());
                        Collection<ModelOTH> modelOTHList = printModel.getModelOTHSet();
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
