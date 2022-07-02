package com.example.ModelView.services.create.locale;

import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.FolderScanRepository;
import com.example.ModelView.persistance.repositories.locale.ModelRepository;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryOth;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryZip;
//import com.example.ModelView.services.lokal.JProgressBarService;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.create.EntitiesAttributeService;
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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.ModelView.utillity.Constant.Create.ZIP_FORMATS;
import static com.example.ModelView.utillity.CreateUtils.*;


@Service
@RequiredArgsConstructor
@Log4j2
public class CreateSyncObjService {

    private final ModelRepository modelRepository;
    private final ModelRepositoryZip modelRepositoryZip;
    private final ModelRepositoryOth modelRepositoryOth;

    private final FolderScanRepository folderScanRepository;
    private final EntitiesAttributeService entitiesAttributeService;
    private final CollectionsService collectionsService;

//    private final JProgressBarService jProgressBarService;
    private final JsProgressBarService jsProgressBarService;

    CopyOnWriteArraySet<PrintModelData> printModelsToSaveSetData;
    CopyOnWriteArraySet<PrintModelOthData> printModelOthDataToSaveSet;
    CopyOnWriteArraySet<PrintModelZipData> printModelZipDataToSaveSet;
    CopyOnWriteArraySet<String> printModelsToSaveNameStringSet;

    HashSet<String> printModelsSavedNameStringSet;
    HashSet<String> printModelsSavedFilesNameStringSet;
    HashSet<String> printModelsSavedFilesAdressStringSet;

    @PostConstruct
    private void postConstruct() {
        printModelsToSaveSetData = collectionsService.getPrintModelsToSaveListData();
        printModelOthDataToSaveSet = collectionsService.getPrintModelOthDataList();
        printModelZipDataToSaveSet = collectionsService.getPrintModelZipDataList();
        printModelsToSaveNameStringSet = collectionsService.getPrintModelsToSaveNameStringSet();
        printModelsSavedNameStringSet = collectionsService.getPrintModelsSavedNameStringSet();
        printModelsSavedFilesNameStringSet = collectionsService.getPrintModelsSavedFilesNameStringSet();
        printModelsSavedFilesAdressStringSet = collectionsService.getPrintModelsSavedFilesAdressStringSet();
    }

    @Transactional
    public void startSyncOBJRepository() throws IOException {

        Collection<File> filesList = folderScanRepository.startScanRepository(true);

        printModelsToSaveNameStringSet.addAll(modelRepository.getAllNameModel());
        //TODO ?
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryOth.getAllnameModelOTH());
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryZip.getAllnameModelZIP());
        printModelsSavedFilesAdressStringSet.addAll(modelRepositoryOth.getAllmodelOTHAdress());
        printModelsSavedFilesAdressStringSet.addAll(modelRepositoryZip.getAllmodelZIPAdress());
        printModelsSavedNameStringSet.addAll(modelRepository.getAllNameModel());

        int filesListSize = filesList.size();
        int taskSize = Math.abs(filesListSize - printModelsSavedFilesAdressStringSet.size());
        AtomicInteger countDone = new AtomicInteger(0);

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
                countDone.incrementAndGet();
                //newProgressBar.updateBar(countDone); windows bar
                JsProgressBarService.setCurrentCount(countDone);
                JsProgressBarService.setCurrentTask(countDone + "/" + filesListSize + " - sync - " + file.getName());
                System.out.println(countDone + "/" + filesListSize + " - sync - " + file.getName());
            }
        }

        printModelsToSaveSetData.forEach(model -> entitiesAttributeService.detectCreateObjTag(model.getModelDirectory())); // Tags
        printModelsToSaveSetData.forEach(model -> entitiesAttributeService.assignTags(model)); // Tags

        collectionsService.saveAllListToJpaRepository();

        //TODO ? Очередность
        checkAndDeleteOBJinListOTHandZIP(filesList);
        deleteModelsWhereDeletedFiles(filesList);

        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveSetData.size());

    }

    public void checkAndCreateOBJ(File file) {

        if (ZIP_FORMATS.contains(FilenameUtils.getExtension(file.getName()))) {
            createModelZIP(file);
        } else {
            createModelOTH(file);
        }
    }

    public void createPrintModelOBJ(File file) {
        String category = detectPrintModelCategory(file);
        PrintModelData printModelData = new PrintModelData(file.getParentFile().getName(), file.getParent(), category);
        printModelData.setMyRate(detectMyRateForModel(file.getParentFile().getName()));
        printModelsToSaveNameStringSet.add(file.getParentFile().getName());
        printModelsToSaveSetData.add(printModelData);
    }

    public void createModelOTH(File file) {
        Double size = getSizeFileToDouble(file);
        String format = FilenameUtils.getExtension(file.getName());
        PrintModelOthData printModelOthData = new PrintModelOthData(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size);
        printModelOthDataToSaveSet.add(printModelOthData);
        String nameModel = file.getParentFile().getName();
        if (printModelsSavedNameStringSet.contains(nameModel)) {
            addInSavedModelOTHList(printModelOthData, nameModel);
        } else {
            addToSaveModelListOTH(file, printModelOthData);
        }
    }

    public void createModelZIP(File file) {
        Double size = getSizeFileToDouble(file);
        String format = FilenameUtils.getExtension(file.getName());
        int ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        PrintModelZipData printModelZipData = new PrintModelZipData(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size, ratioZIP);
        printModelZipDataToSaveSet.add(printModelZipData);
        String nameModel = file.getParentFile().getName();
        if (printModelsSavedNameStringSet.contains(nameModel)) {
            addInSavedModelZIPList(printModelZipData, nameModel);
        } else {
            addToSaveModelListZIP(file, printModelZipData);
        }
    }

    public void addInSavedModelZIPList(PrintModelZipData printModelZipData, String nameModel) {
        PrintModelData printModelData = modelRepository.getByModelName(nameModel);
        System.out.println("------addInSavedModelOTHList - " + nameModel + " ---to--- " + printModelData.getModelName());

        Set<PrintModelZipData> printModelZipDataSet = printModelData.getPrintModelZipDataSet();
        printModelZipDataSet.add(printModelZipData);

        printModelsToSaveSetData.add(printModelData);
    }

    public void addInSavedModelOTHList(PrintModelOthData printModelOthData, String nameModel) {
        PrintModelData printModelData = modelRepository.getByModelName(nameModel);
        System.out.println("------addInSavedModelOTHList - " + nameModel + " -to- " + printModelData.getModelName());

        Set<PrintModelOthData> printModelOthDataSet = printModelData.getPrintModelOthDataSet();
        printModelOthDataSet.add(printModelOthData);

        printModelsToSaveSetData.add(printModelData);
    }

    public void addToSaveModelListZIP(File file, PrintModelZipData printModelZipData) {
        for (PrintModelData printModelData : printModelsToSaveSetData) {
            if (printModelData.getModelName().equals(file.getParentFile().getName())) {
                Set<PrintModelZipData> printModelZipDataSet = printModelData.getPrintModelZipDataSet();
                printModelZipDataSet.add(printModelZipData);
                break;
            }
        }
    }

    public void addToSaveModelListOTH(File file, PrintModelOthData printModelOthData) {
        for (PrintModelData printModelData : printModelsToSaveSetData) {
            if (printModelData.getModelName().equals(file.getParentFile().getName())) {
                Set<PrintModelOthData> printModelOthDataSet = printModelData.getPrintModelOthDataSet();
                printModelOthDataSet.add(printModelOthData);
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
        modelRepository.deleteAllByModelNameIn(deleteModelSet);
    }

    public void checkAndDeleteOBJinListOTHandZIP(Collection<File> files) {

        Collection<PrintModelOthData> toDeleteOTHList = new ArrayList<>();
        Collection<PrintModelZipData> toDeleteZIPList = new ArrayList<>();

        for (File file : files) {
            printModelsSavedFilesAdressStringSet.remove(file.getAbsolutePath());
        }

        if (!printModelsSavedFilesAdressStringSet.isEmpty()) {
            for (String fileAbsPath : printModelsSavedFilesAdressStringSet) {
                if (ZIP_FORMATS.contains(FilenameUtils.getExtension(fileAbsPath))) {
                    try {
                        PrintModelZipData printModelZipData = modelRepositoryZip.getModelZipDataByZipAddress(fileAbsPath);
                        PrintModelData printModelData = modelRepository.getByModelName(printModelZipData.getModelName());
                        Collection<PrintModelZipData> printModelZipDataList = printModelData.getPrintModelZipDataSet();
                        printModelZipDataList.remove(printModelZipData);
                        toDeleteZIPList.add(printModelZipData);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        PrintModelOthData printModelOthData = modelRepositoryOth.getPrintModelOthByOthAddress(fileAbsPath);
                        PrintModelData printModelData = modelRepository.getByModelName(printModelOthData.getModelName());
                        Collection<PrintModelOthData> printModelOthDataList = printModelData.getPrintModelOthDataSet();
                        printModelOthDataList.remove(printModelOthData);
                        toDeleteOTHList.add(printModelOthData);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            modelRepositoryOth.deleteAll(toDeleteOTHList);
            modelRepositoryZip.deleteAll(toDeleteZIPList);
        }
    }
}
