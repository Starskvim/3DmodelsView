package com.example.ModelView.services.create.locale;

import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.FolderScanRepository;
import com.example.ModelView.persistance.repositories.locale.ModelRepository;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.create.EntitiesAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.ModelView.utillity.Constant.Create.ZIP_FORMATS;
import static com.example.ModelView.utillity.CreateUtils.*;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreateObjService {

    private final ModelRepository modelRepository;
    private final FolderScanRepository folderScanRepository;
    private final EntitiesAttributeService entitiesAttributeService;
    private final CollectionsService collectionsService;

    private final JsProgressBarService jsProgressBarService;

    private int filesListSize = 0;
    private volatile AtomicInteger countDone = new AtomicInteger(0);


    CopyOnWriteArraySet<PrintModelData> printModelsToSaveSetData;
    CopyOnWriteArraySet<PrintModelOthData> printModelOthDataToSaveSet;
    CopyOnWriteArraySet<PrintModelZipData> printModelZipDataToSaveSet;
    CopyOnWriteArraySet<String> printModelsToSaveNameStringSet;


    @PostConstruct
    private void postConstruct() {
        printModelsToSaveSetData = collectionsService.getPrintModelsToSaveListData();
        printModelOthDataToSaveSet = collectionsService.getPrintModelOthDataList();
        printModelZipDataToSaveSet = collectionsService.getPrintModelZipDataList();
        printModelsToSaveNameStringSet = collectionsService.getPrintModelsToSaveNameStringSet();
    }

    public void startCreateOBJService() {
        Collection<File> filesList = folderScanRepository.startScanRepository(true);
        createOBJService(filesList);

        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveSetData.size());
    }

    public Set<PrintModelData> startCreateOBJService(Collection<File> filesList) {
        createOBJService(filesList);

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveSetData.size());

        return printModelsToSaveSetData;
    }

    public void createOBJService(Collection<File> filesList) {

//        printModelsToSaveNameStringSet.addAll(modelRepositoryJPA.getAllNameModel()); TODO ???

        filesListSize = filesList.size();
        JsProgressBarService.setTotalCount(filesListSize);

        entitiesAttributeService.prepareDetectTags();
        filesList.parallelStream().forEach(file -> detectTypeCreate(file));
        printModelsToSaveSetData.parallelStream().forEach(model -> entitiesAttributeService.detectCreateObjTag(model.getModelDirectory()));
        printModelsToSaveSetData.parallelStream().forEach(model -> entitiesAttributeService.assignTags(model));


        collectionsService.saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsToSaveSetData.size());

    }

    private void detectTypeCreate(File file) {

        if (collectionsService.checkPrintModelsNameStringSet(file.getParentFile().getName())) {
            checkAndCreateOBJ(file);
        } else {
            createPrintModelOBJ(file);
            checkAndCreateOBJ(file);
            System.err.println("Model - create - " + file.getParentFile().getName());
        }
        countDone.incrementAndGet();

        JsProgressBarService.setCurrentCount(countDone);
        JsProgressBarService.setCurrentTask(countDone + "/" + filesListSize + " - create - " + file.getName());
        System.out.println(countDone + "/" + filesListSize + " - create - " + file.getName());

    }


    private void checkAndCreateOBJ(File file) {
        if (ZIP_FORMATS.contains(getExtension(file.getName()))) {
            createModelZIP(file);
        } else {
            createModelOTH(file);
        }
    }

    private void createPrintModelOBJ(File file) {
        String category = detectPrintModelCategory(file);
        PrintModelData printModelData = new PrintModelData(file.getParentFile().getName(), file.getParent(), category);
        printModelData.setMyRate(detectMyRateForModel(file.getParentFile().getName()));
        printModelsToSaveNameStringSet.add(file.getParentFile().getName());
        printModelsToSaveSetData.add(printModelData);
    }

    private void createModelOTH(File file) {
        Double size = getSizeFileToDouble(file);
        String format = getExtension(file.getName());
        PrintModelOthData printModelOthData = new PrintModelOthData(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size);
        printModelOthDataToSaveSet.add(printModelOthData);
        getModelListOTHRepositoryService(file, printModelOthData);
    }

    private void createModelZIP(File file) {
        Double size = getSizeFileToDouble(file);
        String format = getExtension(file.getName());
        int ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        PrintModelZipData printModelZipData = new PrintModelZipData(file.getName(), file.getParentFile().getName(), file.getAbsolutePath(), format, size, ratioZIP);
        printModelZipDataToSaveSet.add(printModelZipData);
        getModelListZIPService(file, printModelZipData);
    }


    private void getModelListZIPService(File file, PrintModelZipData printModelZipData) {
        for (PrintModelData printModelData : printModelsToSaveSetData) {
            if (printModelData.getModelName().equals(file.getParentFile().getName())) {

                Set<PrintModelZipData> printModelZipDataSet = printModelData.getPrintModelZipDataSet();
                printModelZipDataSet.add(printModelZipData);

                break;
            }
        }
    }

    private void getModelListOTHRepositoryService(File file, PrintModelOthData printModelOthData) {
        for (PrintModelData printModelData : printModelsToSaveSetData) {
            if (printModelData.getModelName().equals(file.getParentFile().getName())) {

                Set<PrintModelOthData> printModelOthDataSet = printModelData.getPrintModelOthDataSet();
                printModelOthDataSet.add(printModelOthData);

                break;
            }
        }
    }


}
