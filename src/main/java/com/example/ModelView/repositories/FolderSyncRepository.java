package com.example.ModelView.repositories;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import lombok.RequiredArgsConstructor;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FolderSyncRepository {
    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;

    Collection<PrintModel> printModelsToSaveList = new ArrayList<>();

    //Collection<PrintModel> printModelsSavedList = new ArrayList<>();


    Collection<ModelOTH> modelOTHList = new ArrayList<>();
    Collection<ModelZIP> modelZIPList = new ArrayList<>();
    ArrayList<String> zipFormatList = new ArrayList<>(6);

    HashSet<String> printModelsToSaveNameStringSet = new HashSet<>(10000);
    HashSet<String> printModelsSavedNameStringSet = new HashSet<>(10000);
    HashSet<String> printModelsSavedFilesNameStringSet = new HashSet<>(30000);

    public Collection<File> startScanSyncRepository() throws IOException {
        long start = System.currentTimeMillis();
        File adres = new File("F:\\[3D PRINT]\\Модели\\[Patreon]\\[Figure]");
        File adres2 = new File("F:\\[3D PRINT]\\Модели\\[Patreon]\\[Pack]");
        File adres3 = new File("F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]");
        Collection<File> files = FileUtils.streamFiles(adres, true, null).collect(Collectors.toList());
        Collection<File> files2 = FileUtils.streamFiles(adres2, true, null).collect(Collectors.toList());
        Collection<File> files3 = FileUtils.streamFiles(adres3, true, null).collect(Collectors.toList());
        files.addAll(files2);
        files.addAll(files3);
        long fin = System.currentTimeMillis();
        System.out.println("ScanRepository SIZE " + files.size());
        System.out.println("ScanRepository TIME " + (fin - start));
        return files;
    }

    public void startSyncOBJRepository() throws IOException {

        Collection<File> filesList = startScanSyncRepository();
        zipFormatList.add("zip");
        zipFormatList.add("7z");
        zipFormatList.add("rar");

        printModelsToSaveNameStringSet.addAll(modelRepositoryJPA.getAllnameModel());

        printModelsSavedFilesNameStringSet.addAll(modelRepositoryOTHJPA.getAllnameModelOTH());
        printModelsSavedFilesNameStringSet.addAll(modelRepositoryZIPJPA.getAllnameModelZIP());

        printModelsSavedNameStringSet.addAll(modelRepositoryJPA.getAllnameModel());

        int filesListSize = filesList.size();
        int countDone = 0;

        for (File file : filesList) {
            if (checkPrintModelsFilesSavedNameStringSet(file.getName())) {
                if (checkSyncPrintModelsNameStringSet(file.getParentFile().getName())) {
                    checkAndCreateOBJ(file);
                } else {
                    createPrintModelOBJ(file);
                    checkAndCreateOBJ(file);
                }
                countDone += 1;
                System.out.println(countDone + "/" + filesListSize + " - sync - " + file.getName());
            }
        }

        saveAllListToJpaRepository();



        System.out.println( "Входные файлы filesList size - " + filesList.size());
        System.out.println( "Итоговые модели printModelsList size - " + printModelsToSaveList.size());

    }

    public void saveAllListToJpaRepository () {

        long start = System.currentTimeMillis();

        long start1 = System.currentTimeMillis();
        modelRepositoryJPA.saveAll(printModelsToSaveList);
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

    public boolean checkSyncPrintModelsNameStringSet(String name){
        if (printModelsToSaveNameStringSet.isEmpty()) {
            return false;
        } else if (printModelsToSaveNameStringSet.contains(name)){
            return true;
        }
        return false;
    }

    public String detectPrintModelCategory (File file) {
        if (file.getPath().contains("[Figure]")) {
            return "[Figure]";
        } else if (file.getPath().contains("[Pack]")) {
            return "[Pack]";
        } else if (file.getPath().contains("[Other]")) {
            return "[OtherFDM]";
        } else {
            return "Other";
        }
    }

    public void checkAndCreateOBJ(File file) {
        if (zipFormatList.contains(FilenameUtils.getExtension(file.getName()))) {
            createModelZIP(file);
        } else {
            createModelOTH(file);
        }
    }

    public void createPrintModelOBJ(File file) {
        PrintModel printModel = new PrintModel(file.getParentFile().getName(), file.getParent(), detectPrintModelCategory(file));

        printModelsToSaveNameStringSet.add(file.getParentFile().getName());

        printModelsToSaveList.add(printModel);
    }

    public void createModelOTH(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getAbsolutePath(), format, size);
        modelOTHList.add(modelOTH);

        String nameModel = file.getParentFile().getName();


        if (printModelsSavedFilesNameStringSet.contains(nameModel)){
            addInSavedModelOTHList(modelOTH, nameModel);
        }else {
            addToSaveModelListOTH(file, modelOTH);
        }

    }

    public void createModelZIP(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        double ratioZIP = getArchiveCompressionRatio(file.getAbsolutePath());
        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPList.add(modelZIP);

        String nameModel = file.getParentFile().getName();

        if (printModelsSavedFilesNameStringSet.contains(nameModel)){
            addInSavedModelZIPList(modelZIP, nameModel);
        }else {
            addToSaveModelListZIP(file, modelZIP);
        }


    }

    public void addInSavedModelZIPList (ModelZIP modelZIP, String nameModel){

            PrintModel printModel = modelRepositoryJPA.getBynameModel(nameModel);

            printModel.addModelZIP(modelZIP);

            modelRepositoryJPA.save(printModel);

    }

    public void addInSavedModelOTHList (ModelOTH modelOTH, String nameModel) {

            PrintModel printModel = modelRepositoryJPA.getBynameModel(nameModel);

            printModel.addModelOTH(modelOTH);

            modelRepositoryJPA.save(printModel);

    }

    public double getArchiveCompressionRatio (String sourceZipFile) {
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(sourceZipFile, "r");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

            double allsize = 0;
            double allOrigsize = 0;

            for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                try {
                    allsize += item.getSize();
                    allOrigsize += item.getPackedSize();
                } catch (Exception e) {
                    allsize += 0.0;
                    allOrigsize += 0.0;
                }
            }
            double preResult = allOrigsize / allsize;
            return (double) Math.round(preResult * 100.0); ////////
        } catch (Exception e) {
            System.err.println("Error occurs: " + e + "-----" + sourceZipFile);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    System.err.println("Error closing archive: " + e);
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e);
                }
            }
        }
        return 0.0;
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

    public void deleteModelsWhereDeletedFiles (Collection<File> files){

        HashSet<String> deletModelSet = printModelsSavedNameStringSet;

        for (File file : files){
            deletModelSet.remove(file.getParentFile().getName());
            System.out.println(file.getParentFile().getName() + "- модель удалена");
        }

        modelRepositoryJPA.deleteAllBynameModel(deletModelSet);

    }

    public boolean checkPrintModelsFilesSavedNameStringSet(String name){
        if (printModelsSavedFilesNameStringSet == null){
            return true;
        }
        else if (printModelsSavedFilesNameStringSet.isEmpty()) {
            return true;
        }else if (printModelsSavedFilesNameStringSet.contains(name)) {
            return false;
        }
        return true;
    }
}
