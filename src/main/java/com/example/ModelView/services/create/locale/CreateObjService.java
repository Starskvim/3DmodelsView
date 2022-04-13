package com.example.ModelView.services.create.locale;

import com.example.ModelView.entities.locale.ModelOTH;
import com.example.ModelView.entities.locale.ModelZIP;
import com.example.ModelView.entities.locale.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.repositories.jpa.locale.ModelRepositoryJPA;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.create.EntitiesAttributeService;
import com.example.ModelView.services.create.locale.CollectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

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
    private volatile AtomicInteger countDone = new AtomicInteger(0);


    CopyOnWriteArraySet<PrintModel> printModelsToSaveSet;
    CopyOnWriteArraySet<ModelOTH> modelOTHToSaveSet;
    CopyOnWriteArraySet<ModelZIP> modelZIPToSaveSet;
    CopyOnWriteArrayList<String> zipFormatList;
    CopyOnWriteArraySet<String> printModelsToSaveNameStringSet;


    @PostConstruct
    private void postConstruct() {
        printModelsToSaveSet = collectionsService.getPrintModelsToSaveList();
        modelOTHToSaveSet = collectionsService.getModelOTHList();
        modelZIPToSaveSet = collectionsService.getModelZIPList();
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

        entitiesAttributeService.prepareDetectTags();
        filesList.parallelStream().forEach(file -> detectTypeCreate(file));
        printModelsToSaveSet.parallelStream().forEach(model -> entitiesAttributeService.detectCreateObjTag(model.getModelDerictory()));
        printModelsToSaveSet.parallelStream().forEach(model -> entitiesAttributeService.assignTags(model));


        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveSet.size());

    }

    private void detectTypeCreate(File file) {

        if (collectionsService.checkPrintModelsNameStringSet(file.getParentFile().getName())) {
            checkAndCreateOBJ(file);
        } else {
            createPrintModelOBJ(file);
            checkAndCreateOBJ(file);
        }
        countDone.incrementAndGet();

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
        PrintModel printModel = new PrintModel(file.getParentFile().getName(), file.getParent(), category);
        printModel.setMyRate(entitiesAttributeService.detectMyRateForModel(file.getParentFile().getName()));
        printModelsToSaveNameStringSet.add(file.getParentFile().getName());
        printModelsToSaveSet.add(printModel);
    }

    private void createModelOTH(File file) {
        Double size = entitiesAttributeService.getSizeFileToDouble(file);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size);
        modelOTHToSaveSet.add(modelOTH);
        getModelListOTHRepositoryService(file, modelOTH);
    }

    private void createModelZIP(File file) {
        Double size = entitiesAttributeService.getSizeFileToDouble(file);
        String format = FilenameUtils.getExtension(file.getName());
        int ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPToSaveSet.add(modelZIP);
        getModelListZIPService(file, modelZIP);
    }


    private void getModelListZIPService(File file, ModelZIP modelZip) {
        for (PrintModel printModel : printModelsToSaveSet) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {

                Set<ModelZIP> modelZIPSet = printModel.getModelZIPSet();
                modelZIPSet.add(modelZip);

                break;
            }
        }
    }

    private void getModelListOTHRepositoryService(File file, ModelOTH modelOTH) {
        for (PrintModel printModel : printModelsToSaveSet) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {

                Set<ModelOTH> modelOTHSet = printModel.getModelOTHSet();
                modelOTHSet.add(modelOTH);

                break;
            }
        }
    }


}
