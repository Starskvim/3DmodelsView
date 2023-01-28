package com.example.ModelView.services.lokal;

import com.example.ModelView.mapping.MapperDto;
import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.FolderScanRepository;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.PrintModelService;
import com.example.ModelView.services.create.locale.CollectionsService;
import com.example.ModelView.services.create.web.CreateWebService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class SerializeService {

    private final CreateWebService createWebService;
    private final FolderScanRepository folderScanRepository;
    private final CollectionsService collectionsService;
    private final JsProgressBarService jsProgressBarService;
    private final MapperDto mapperDTO;
    private final PrintModelService printModelService;

    private final ObjectMapper objectMapper;

    private static Integer total = 0;
    private static volatile AtomicInteger count = new AtomicInteger(0);

    @Value("${scan.addressSer}")
    private String addressSer;

    public void serializeOneModelToWebDtoService(Long id) {
        PrintModelData printModelData = printModelService.getById(id);
        try {
            serializeDtoAndSave(mapperDTO.toPrintModelWebDto(printModelData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serializeObj(Collection<PrintModelData> outputList) {

        total = outputList.size();
        JsProgressBarService.setTotalCount(total);

        outputList.stream().parallel().forEach(printModel -> streamSerialize(printModel));
    }

    private void streamSerialize(PrintModelData printModelData) { // TODO need test
        PrintModelWeb printModelWeb = mapperDTO.toPrintModelWebDto(printModelData);
        String modelName = printModelWeb.getModelName();
        String modelString = null;
        try {
            modelString = objectMapper.writeValueAsString(printModelWeb);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.writeStringToFile(new File(addressSer + "/" + modelName + ".json"), modelString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" serializeObj " + modelName);

        count.incrementAndGet();

        JsProgressBarService.setCurrentCount(count);
        JsProgressBarService.setCurrentTask(count + "/" + total + " - ser - " + printModelData.getModelName());
        System.out.println(count + "/" + total + " serializeObj " + printModelData.getModelName());
    }

    public void serializeDtoAndSave(PrintModelWeb printModelWeb) throws IOException {
        String modelName = printModelWeb.getModelName();
        String modelString = objectMapper.writeValueAsString(printModelWeb);
        FileUtils.writeStringToFile(new File(addressSer + "/" + modelName + ".json"), modelString);
        System.out.println(" serializeObj " + modelName);

    }

    public void deserializeObj() throws IOException, ClassNotFoundException {
        Collection<File> inputSer = folderScanRepository.startScanRepository(false);
        AtomicInteger count = new AtomicInteger(0);
        int total = inputSer.size();
        JsProgressBarService.setTotalCount(total);

        for (File file : inputSer) {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());

            PrintModelWeb printModelWeb = objectMapper.readValue(fileInputStream, PrintModelWeb.class);
            fileInputStream.close();

            createWebService.addNewModel(printModelWeb);

            count.incrementAndGet();
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModelWeb.getModelName());
            System.out.println(count + "/" + total + " deserializeObj " + printModelWeb.getModelName());
        }
    } // TODO not working ......

    public void deserializeObjDTO(byte[] bytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        PrintModelWeb printModelWeb = (PrintModelWeb) objectInputStream.readObject();
        objectInputStream.close();

        JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModelWeb.getModelName());
        System.out.println(count + "/" + total + " deserializeObj " + printModelWeb.getModelName());

        System.out.println(printModelWeb.getModelOthList().size() + " size list");

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

    public void deserializePrintModelWebDTO(byte[] bytes) throws IOException{

        PrintModelWeb printModelWeb = objectMapper.readValue(bytes, PrintModelWeb.class);


    }


}
