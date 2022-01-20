package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.repositories.ModelRepositoryJPA;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class SerializeService {
    private final FolderScanRepository folderScanRepository;
    private final CollectionsService collectionsService;
    private final JsProgressBarService jsProgressBarService;

    private static Integer total = 0;
    private static volatile int  count = 0;

    @Value("${scan.adressSer}")
    private String adressSer;

    public void serializeObj(List<PrintModel> outputList) throws IOException {


        total = outputList.size();
        JsProgressBarService.setTotalCount(total);

        for (PrintModel printModel : outputList) {


            FileOutputStream outputStream = new FileOutputStream(adressSer + "/" + printModel.getModelName() +".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(printModel);
            objectOutputStream.close();

            count ++;

            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - ser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " serializeObj " + printModel.getModelName());


        }
    }

    @Transactional
    public void deserializeObj() throws IOException, ClassNotFoundException {

        CopyOnWriteArraySet<PrintModel> printModelsToSaveList = collectionsService.getPrintModelsToSaveList();
        CopyOnWriteArraySet<ModelOTH> modelOTHList = collectionsService.getModelOTHList();
        CopyOnWriteArraySet<ModelZIP> modelZIPList = collectionsService.getModelZIPList();

        Collection<File> inputSer = folderScanRepository.startScanRepository(false);

        int count = 0;
        int total = inputSer.size();
        JsProgressBarService.setTotalCount(total);

        for (File file: inputSer) {

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            PrintModel printModel = (PrintModel) objectInputStream.readObject();
            objectInputStream.close();

            printModel.setId(0L);

            for (ModelZIP modelZIP: printModel.getModelZIPSet()){
                modelZIP.setId(0L);
            }
            for (ModelOTH modelOTH: printModel.getModelOTHSet()){
                modelOTH.setId(0L);
            }

            printModelsToSaveList.add(printModel);

            count ++;
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " deserializeObj " + printModel.getModelName());
        }

        System.out.println(printModelsToSaveList.size() + " printModelsToSaveList");
        System.out.println(modelOTHList.size() + " modelOTHList");
        System.out.println(modelZIPList.size() + " modelZIPList");

        collectionsService.saveAllListToJpaRepository();

    }
}
