package com.example.ModelView.services;

import com.example.ModelView.dto.web.ModelOTHWebDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
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
    private static volatile int count = 0;

    @Value("${scan.adressSer}")
    private String adressSer;

    public void serializeObj(List<PrintModel> outputList) throws IOException {


        total = outputList.size();
        JsProgressBarService.setTotalCount(total);

        for (PrintModel printModel : outputList) {


            FileOutputStream outputStream = new FileOutputStream(adressSer + "/" + printModel.getModelName() + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(printModel);
            objectOutputStream.close();

            count++;

            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - ser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " serializeObj " + printModel.getModelName());


        }
    }

    public void serializeDTO(PrintModelWebDTO printModelWebDTO) throws IOException {

//        total = outputList.size();
//        JsProgressBarService.setTotalCount(total);

        String modelName = printModelWebDTO.getModelName();
        FileOutputStream outputStream = new FileOutputStream(adressSer + "/" + modelName + "WEB.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(printModelWebDTO);
        objectOutputStream.close();

//        count++;
//        JsProgressBarService.setCurrentCount(count);
        JsProgressBarService.setCurrentTask(" - ser - " + modelName);
        System.out.println(" serializeObj " + modelName);


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

        for (File file : inputSer) {

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            PrintModel printModel = (PrintModel) objectInputStream.readObject();
            objectInputStream.close();

            printModel.setId(0L);

            for (ModelZIP modelZIP : printModel.getModelZIPSet()) {
                modelZIP.setId(0L);
            }
            for (ModelOTH modelOTH : printModel.getModelOTHSet()) {
                modelOTH.setId(0L);
            }

            printModelsToSaveList.add(printModel);

            count++;
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModel.getModelName());
            System.out.println(count + "/" + total + " deserializeObj " + printModel.getModelName());
        }

        System.out.println(printModelsToSaveList.size() + " printModelsToSaveList");
        System.out.println(modelOTHList.size() + " modelOTHList");
        System.out.println(modelZIPList.size() + " modelZIPList");

        collectionsService.saveAllListToJpaRepository();

    }

    public void deserializeObjDTO(byte[] bytes) throws IOException, ClassNotFoundException {


        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        PrintModelWebDTO printModelWebDTO = (PrintModelWebDTO) objectInputStream.readObject();
        objectInputStream.close();

        JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModelWebDTO.getModelName());
        System.out.println(count + "/" + total + " deserializeObj " + printModelWebDTO.getModelName());

        System.out.println(printModelWebDTO.getTotalSize());
        System.out.println(printModelWebDTO.getModelOTHList().size() + " size list");


        List<String> list = printModelWebDTO.getModelTags();

        for(String tag: list){
            System.out.println(tag);
        }

    }
}
