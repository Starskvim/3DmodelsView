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
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreateObjService {

    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;
    private final FolderScanRepository folderScanRepository;
    private final EntitiesAttributeService entitiesAttributeService;

    Collection<PrintModel> printModelsList = new ArrayList<>();

    Collection<ModelOTH> modelOTHList = new ArrayList<>();
    Collection<ModelZIP> modelZIPList = new ArrayList<>();
    ArrayList<String> zipFormatList = new ArrayList<>(6);

    HashSet<String> printModelsNameStringSet = new HashSet<>(10000);

    public void startCreateOBJService() throws IOException {

        Collection<File> filesList = folderScanRepository.startScanRepository();
        zipFormatList.add("zip");
        zipFormatList.add("7z");
        zipFormatList.add("rar");

        printModelsNameStringSet.addAll(modelRepositoryJPA.getAllNameModel());


        int filesListSize = filesList.size();
        int countDone = 0;

        for (File file : filesList) {
            if (checkPrintModelsNameStringSet(file.getParentFile().getName())) {
                checkAndCreateOBJ(file);
            } else {
                createPrintModelOBJ(file);
                checkAndCreateOBJ(file);
            }
            countDone += 1;
            System.out.println(countDone + "/" + filesListSize + " - " + file.getName());
        }

        saveAllListToJpaRepository();

        log.info("Входные файлы filesList size - {}", filesList.size());
        log.info("Итоговые модели printModelsList size - {}", printModelsList.size());

    }

    public void saveAllListToJpaRepository() {

        long start = System.currentTimeMillis();

        long start1 = System.currentTimeMillis();
        modelRepositoryJPA.saveAll(printModelsList);
        long fin1 = System.currentTimeMillis();
        System.out.println("modelRepositoryJPA.saveAll time - " + (fin1 - start1));

        long start2 = System.currentTimeMillis();
        modelRepositoryZIPJPA.saveAll(modelZIPList);
        long fin2 = System.currentTimeMillis();
        System.out.println("modelRepositoryZIPJPA.saveAll time - " + (fin2 - start2));

        long start3 = System.currentTimeMillis();
        modelRepositoryOTHJPA.saveAll(modelOTHList);
        long fin3 = System.currentTimeMillis();
        System.out.println("modelRepositoryOTHJPA.saveAll time - " + (fin3 - start3));

        long fin = System.currentTimeMillis();
        System.out.println("ALL SAVE saveAllListToJpaRepository time - " + (fin - start));

    }

    public boolean checkPrintModelsNameStringSet(String name) {
        if (printModelsNameStringSet.isEmpty()) {
            return false;
        } else return printModelsNameStringSet.contains(name);
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

        printModelsNameStringSet.add(file.getParentFile().getName());

        printModelsList.add(printModel);
    }

    public void createModelOTH(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getAbsolutePath(), format, size);
        modelOTHList.add(modelOTH);
        getModelListOTHRepositoryRepository(file, modelOTH);
    }

    public void createModelZIP(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        double ratioZIP = entitiesAttributeService.getCreateArchiveCompressionRatio(file.getAbsolutePath());
        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPList.add(modelZIP);
        getModelListZIPRepository(file, modelZIP);
    }



    public void getModelListZIPRepository(File file, ModelZIP modelZip) {
        for (PrintModel printModel : printModelsList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelZIP(modelZip);
                break;
            }
        }
    }

    public void getModelListOTHRepositoryRepository(File file, ModelOTH modelOTH) {
        for (PrintModel printModel : printModelsList) {
            if (printModel.getModelName().equals(file.getParentFile().getName())) {
                printModel.addModelOTH(modelOTH);
                break;
            }
        }
    }


}
