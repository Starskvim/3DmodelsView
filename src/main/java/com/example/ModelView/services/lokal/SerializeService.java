package com.example.ModelView.services.lokal;

import com.example.ModelView.dto.MapperDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.FolderScanRepository;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.PrintModelService;
import com.example.ModelView.services.create.CollectionsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private final MapperDTO mapperDTO;
    private final PrintModelService printModelService;

    private final ObjectMapper objectMapper;

    private static Integer total = 0;
    private static volatile int count = 0;

    @Value("${scan.adressSer}")
    private String adressSer;

    public void serializeOneModelToWebDtoService(Long id) {
        PrintModel printModel = printModelService.getById(id);
        try {
            serializeDTO(mapperDTO.toPrintModelWebDTO(printModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        String modelString = objectMapper.writeValueAsString(printModelWebDTO);
        FileUtils.writeStringToFile(new File(adressSer + "/" + modelName + "WEB.json"), modelString);

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

    } // TODO not working

    public void deserializeObjDTO(byte[] bytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        PrintModelWebDTO printModelWebDTO = (PrintModelWebDTO) objectInputStream.readObject();
        objectInputStream.close();

        JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModelWebDTO.getModelName());
        System.out.println(count + "/" + total + " deserializeObj " + printModelWebDTO.getModelName());

        System.out.println(printModelWebDTO.getModelOTHList().size() + " size list");

    } // TODO not working

    public void handleFileUploadService(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
//                BufferedOutputStream stream =
//                        new BufferedOutputStream(new FileOutputStream("uploaded"));
//                stream.write(bytes);
//                stream.close();
                deserializeObjDTO(bytes);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Пустой файл");
        }
    }


}
