package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.ModelRepositoryJPA;
import com.example.ModelView.repositories.ModelRepositoryOTHJPA;
import com.example.ModelView.repositories.ModelRepositoryZIPJPA;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class CollectionsService {

    private final ModelRepositoryJPA modelRepositoryJPA;
    private final ModelRepositoryZIPJPA modelRepositoryZIPJPA;
    private final ModelRepositoryOTHJPA modelRepositoryOTHJPA;

    private CopyOnWriteArraySet<PrintModel> printModelsToSaveList = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<ModelOTH> modelOTHList = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<ModelZIP> modelZIPList = new CopyOnWriteArraySet<>();
    private CopyOnWriteArrayList<String> zipFormatList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArraySet<String> printModelsToSaveNameStringSet = new CopyOnWriteArraySet<>();


//    private HashSet<PrintModel> printModelsToSaveList = new HashSet<>();
//    private HashSet<ModelOTH> modelOTHList = new HashSet<>();
//    private HashSet<ModelZIP> modelZIPList = new HashSet<>();
//    private ArrayList<String> zipFormatList = new ArrayList<>(6);
//    private HashSet<String> printModelsToSaveNameStringSet = new HashSet<>(10000);



    private HashSet<String> printModelsSavedNameStringSet = new HashSet<>(10000);

    //?
    private HashSet<String> printModelsSavedFilesNameStringSet = new HashSet<>(30000);

    private HashSet<String> printModelsSavedFilesAdressStringSet = new HashSet<>(30000);


    public boolean checkPrintModelsNameStringSet(String name) {
        if (printModelsToSaveNameStringSet.isEmpty()) {
            return false;
        } else return printModelsToSaveNameStringSet.contains(name);
    }

    @Transactional
    public void saveAllListToJpaRepository () {

        long start = System.currentTimeMillis();

        long start1 = System.currentTimeMillis();
        if (!printModelsToSaveList.isEmpty()) {
            modelRepositoryJPA.saveAll(printModelsToSaveList);
        }
        long fin1 = System.currentTimeMillis();
        System.out.println("modelRepositoryJPA.saveAll time - " + (fin1 - start1));

        long start2 = System.currentTimeMillis();
        if (!modelZIPList.isEmpty()) {
            modelRepositoryZIPJPA.saveAll(modelZIPList);
        }
        long fin2 = System.currentTimeMillis();
        System.out.println("modelRepositoryZIPJPA.saveAll time - " + (fin2 - start2));

        long start3 = System.currentTimeMillis();
        if (!modelOTHList.isEmpty()) {
            modelRepositoryOTHJPA.saveAll(modelOTHList);
        }
        long fin3 = System.currentTimeMillis();
        System.out.println("modelRepositoryOTHJPA.saveAll time - " + (fin3 - start3));

        long fin = System.currentTimeMillis();
        System.out.println("ALL SAVE saveAllListToJpaRepository time - " + (fin - start));

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
