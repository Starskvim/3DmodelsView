package com.example.ModelView.repositories;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import lombok.NoArgsConstructor;
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
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class FolderScanRepository {

    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;

    Collection<PrintModel> printModelsList = new ArrayList<>();
    Collection<ModelOTH> modelOTHList = new ArrayList<>();
    Collection<ModelZIP> modelZIPList = new ArrayList<>();
    ArrayList<String> zipFormatList = new ArrayList<>(6);
//////////////////////////////////////// hashset?

    public Collection<File> startScanRepository() throws IOException {
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

    public void startCreateOBJRepository() throws IOException {

        Collection<File> filesList = startScanRepository();
        zipFormatList.add("zip");
        zipFormatList.add("7z");
        zipFormatList.add("rar");


        for (File file : filesList) {
            if (checkPrintModelsList(file)) {
                checkAndCreateOBJ(file);
                System.out.println(file.getName() + "- OK");
            } else {
                createPrintModelOBJ(file);
                checkAndCreateOBJ(file);
                System.out.println(file.getName() + "- OK");
            }
        }

        saveAllListToJpaRepository();

        System.out.println( "Входные файлы filesList size - " + filesList.size());
        System.out.println( "Итоговые модели printModelsList size - " + printModelsList.size());

    }

    public void saveAllListToJpaRepository () {

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

    public boolean checkPrintModelsList(File file) {
        if (CollectionUtils.isEmpty(printModelsList)) {
            return false;
        } else {
            for (PrintModel printModel : printModelsList) {
                if (printModel.getModelName().equals(file.getParentFile().getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createPrintModelOBJ(File file) {
        PrintModel printModel = new PrintModel(file.getParentFile().getName(), file.getParent(), detectPrintModelCategory(file));
        printModelsList.add(printModel);

        // rep
        //modelRepositoryJPA.save(printModel);
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

    public void createModelOTH(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());
        ModelOTH modelOTH = new ModelOTH(file.getName(), file.getAbsolutePath(), format, size);
        modelOTHList.add(modelOTH);

        // rep
        //modelRepositoryOTHJPA.save(modelOTH);


        getModelListOTHRepositoryRepository(file, modelOTH);
    }

    public void createModelZIP(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String size = decimalFormat.format(file.length() / 1024.0 / 1024.0);
        String format = FilenameUtils.getExtension(file.getName());

        double ratioZIP = getArchiveCompressionRatio(file.getAbsolutePath());

        ModelZIP modelZIP = new ModelZIP(file.getName(), file.getAbsolutePath(), format, size, ratioZIP);
        modelZIPList.add(modelZIP);

        // rep
        //modelRepositoryZIPJPA.save(modelZIP);


        getModelListZIPRepository(file, modelZIP);
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
            System.err.println("Error occurs: " + e);
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

// VER 1 TEST
// 1 ScanRepository SIZE 5354 ScanRepository TIME 7262 - 2 ScanRepository SIZE 5354 ScanRepository TIME 188
//                                                       3                          ScanRepository TIME 210
// 4 filesList size - 5354 printModelsList size - 1172  startCreateController time create - 199932
// 5                                                    startCreateController time create - 82257
// 6                                                    startCreateController time create - 86004
// 7                                                    startCreateController time create - 84278

// VER 2 TEST
// 1                                                    startCreateController time create - 4387
//   modelRepositoryJPA.saveAll time - 1175
//   modelRepositoryZIPJPA.saveAll time - 44
//   modelRepositoryOTHJPA.saveAll time - 49
//   ALL SAVE saveAllListToJpaRepository time - 1268
// 2                                                    startCreateController time create - 4334


//all folder
// 3 ScanRepository SIZE 24982 ScanRepository TIME 29750 - startCreateController time create - 381979
//   modelRepositoryJPA.saveAll time - 4721
//   modelRepositoryZIPJPA.saveAll time - 186
//   modelRepositoryOTHJPA.saveAll time - 204
//   ALL SAVE saveAllListToJpaRepository time - 5111
//   Входные файлы filesList size - 24982
//   Итоговые модели printModelsList size - 4044