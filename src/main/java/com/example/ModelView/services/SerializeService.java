package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
public class SerializeService {
    private final FolderScanRepository folderScanRepository;
    private final CollectionsService collectionsService;
    private final JsProgressBarService jsProgressBarService;

    private static Integer total = 0;
    private static volatile int  count = 0;


    public void serializeObj(List<PrintModel> outputList) throws IOException {


        total = outputList.size();
        JsProgressBarService.setTotalCount(total);

        for (PrintModel printModel : outputList) {


            FileOutputStream outputStream = new FileOutputStream("G:\\testJava\\" + printModel.getModelName() +".ser");
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

//        Hibernate.initialize(PrintModel.class);
//        Hibernate.initialize(ModelZIP.class);
//        Hibernate.initialize(ModelOTH.class);



        int count = 0;
        int total = inputSer.size();
        JsProgressBarService.setTotalCount(total);

        for (File file: inputSer) {

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            PrintModel printModel = (PrintModel) objectInputStream.readObject();
            objectInputStream.close();

            Hibernate.initialize(printModel);

            printModelsToSaveList.add(printModel);
            modelOTHList.addAll(printModel.getModelOTHSet());
            modelZIPList.addAll(printModel.getModelZIPSet());

//            for (ModelZIP modelZIP: printModel.getModelZIPSet()){
//                System.out.println(modelZIP.getArchiveRatio());
//            }

            count ++;
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " deserializeObj " + printModel.getModelName());
        }

        collectionsService.saveAllListToJpaRepository();

    }
}