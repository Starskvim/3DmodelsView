package com.example.ModelView.services.lokal;

import com.example.ModelView.mapping.MapperDto;
import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.FolderScanRepository;
import com.example.ModelView.services.JsProgressBarService;
import com.example.ModelView.services.PrintModelService;
import com.example.ModelView.services.create.locale.CollectionsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class SerializeService {
    private final FolderScanRepository folderScanRepository;
    private final CollectionsService collectionsService;
    private final JsProgressBarService jsProgressBarService;
    private final MapperDto mapperDTO;
    private final PrintModelService printModelService;

    private final ObjectMapper objectMapper;

    private static Integer total = 0;
    private static volatile AtomicInteger count = new AtomicInteger(0);

    @Value("${scan.adressSer}")
    private String adressSer;

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
            FileUtils.writeStringToFile(new File(adressSer + "/" + modelName + ".json"), modelString);
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
        FileUtils.writeStringToFile(new File(adressSer + "/" + modelName + ".json"), modelString);
        System.out.println(" serializeObj " + modelName);

    }

    public String serializeDto (PrintModelWeb printModelWeb)  {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(printModelWeb);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Transactional
    public void deserializeObj() throws IOException, ClassNotFoundException {

        CopyOnWriteArraySet<PrintModelData> printModelsToSaveListData = collectionsService.getPrintModelsToSaveListData();
        CopyOnWriteArraySet<PrintModelOthData> printModelOthDataList = collectionsService.getPrintModelOthDataList();
        CopyOnWriteArraySet<PrintModelZipData> printModelZipDataList = collectionsService.getPrintModelZipDataList();

        Collection<File> inputSer = folderScanRepository.startScanRepository(false);

        AtomicInteger count = new AtomicInteger(0);
        int total = inputSer.size();
        JsProgressBarService.setTotalCount(total);

        for (File file : inputSer) {

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            PrintModelData printModelData = (PrintModelData) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();

            printModelData.setId(0L);

            for (PrintModelZipData printModelZipData : printModelData.getPrintModelZipDataSet()) {
                printModelZipData.setId(0L);
            }
            for (PrintModelOthData printModelOthData : printModelData.getPrintModelOthDataSet()) {
                printModelOthData.setId(0L);
            }

            printModelsToSaveListData.add(printModelData);

            count.incrementAndGet();
            JsProgressBarService.setCurrentCount(count);
            JsProgressBarService.setCurrentTask(count + "/" + total + " - deser - " + printModelData.getModelName());
            System.out.println(count + "/" + total + " deserializeObj " + printModelData.getModelName());
        }

        System.out.println(printModelsToSaveListData.size() + " printModelsToSaveList");
        System.out.println(printModelOthDataList.size() + " modelOTHList");
        System.out.println(printModelZipDataList.size() + " modelZIPList");

        collectionsService.saveAllListToJpaRepository();

    } // TODO not working

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
