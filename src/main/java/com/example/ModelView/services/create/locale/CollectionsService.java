package com.example.ModelView.services.create.locale;

import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelTagData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.repositories.locale.ModelRepository;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryOth;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryTags;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryZip;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

@Log4j2
@Setter
@Getter
@Service
@RequiredArgsConstructor
public class CollectionsService {

    private final ModelRepository modelRepository;
    private final ModelRepositoryZip modelRepositoryZip;
    private final ModelRepositoryOth modelRepositoryOth;
    private final ModelRepositoryTags modelRepositoryTags;

    private CopyOnWriteArraySet<PrintModelData> printModelsToSaveListData = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<PrintModelOthData> printModelOthDataList = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<PrintModelZipData> printModelZipDataList = new CopyOnWriteArraySet<>();
//    private CopyOnWriteArrayList<String> zipFormatList = new CopyOnWriteArrayList<>(ZIP_FORMATS);
    private CopyOnWriteArraySet<String> printModelsToSaveNameStringSet = new CopyOnWriteArraySet<>();

    // TODO multi ?
    private HashSet<String> printModelsSavedNameStringSet = new HashSet<>(10000);
    private HashSet<String> printModelsSavedFilesNameStringSet = new HashSet<>(30000);
    private HashSet<String> printModelsSavedFilesAddressStringSet = new HashSet<>(30000);

    private HashSet<PrintModelTagData> modelsTagsSavedSet = new HashSet<>(400);
    private CopyOnWriteArraySet<PrintModelTagData> modelsTagsToSaveSet = new CopyOnWriteArraySet<>();

    public boolean checkPrintModelsNameStringSet(String name) {
        if (printModelsToSaveNameStringSet.isEmpty()) {
            return false;
        } else return printModelsToSaveNameStringSet.contains(name);
    }

    @Transactional
    public void saveAllListToJpaRepository () {

        long start = System.currentTimeMillis();

        long start4 = System.currentTimeMillis();
        if (!modelsTagsToSaveSet.isEmpty()) {
            modelRepositoryTags.saveAll(modelsTagsToSaveSet);
        }
        long fin4 = System.currentTimeMillis();
        log.info("modelRepositoryTagsJPA.saveAll time - " + (fin4 - start4));


//        long start1 = System.currentTimeMillis();
//        if (!printModelsToSaveList.isEmpty()) {
//            modelRepositoryJPA.saveAll(printModelsToSaveList);
//        }
//        long fin1 = System.currentTimeMillis();
//        System.out.println("modelRepositoryJPA.saveAll time - " + (fin1 - start1));

//        long start2 = System.currentTimeMillis();
//        if (!modelZIPList.isEmpty()) {
//            modelRepositoryZIPJPA.saveAll(modelZIPList);
//        }
//        long fin2 = System.currentTimeMillis();
//        System.out.println("modelRepositoryZIPJPA.saveAll time - " + (fin2 - start2));
//
//        long start3 = System.currentTimeMillis();
//        if (!modelOTHList.isEmpty()) {
//            modelRepositoryOTHJPA.saveAll(modelOTHList);
//        }
//        long fin3 = System.currentTimeMillis();
//        System.out.println("modelRepositoryOTHJPA.saveAll time - " + (fin3 - start3));

        long fin = System.currentTimeMillis();
        log.info("All save saveAllListToJpaRepository time - " + (fin4 - start4));
    }

    public boolean checkPrintModelsFilesSavedNameStringSet(String name){
        if (printModelsSavedFilesNameStringSet == null){
            return true;
        }
        else if (printModelsSavedFilesNameStringSet.isEmpty()) {
            return true;
        }else return !printModelsSavedFilesNameStringSet.contains(name);
    }
}
