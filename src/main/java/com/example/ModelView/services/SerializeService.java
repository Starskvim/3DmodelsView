package com.example.ModelView.services;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    public void serializeObj(List<PrintModel> outputList) throws IOException {

        int count = 0;
        int total = outputList.size();
        JsProgressBarService.setTotalCount(total);

        for (PrintModel printModel : outputList) {

            FileOutputStream outputStream = new FileOutputStream("G:\\testJava\\" + printModel.getModelName() +".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(printModel);
            objectOutputStream.close();

            count ++;
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - ser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " serializeObj");


        }
    }

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

            printModelsToSaveList.add(printModel);
            modelOTHList.addAll(printModel.getModelOTHSet());
            modelZIPList.addAll(printModel.getModelZIPSet());

            count ++;
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " deserializeObj");

        }

        collectionsService.saveAllListToJpaRepository();

    }
}
