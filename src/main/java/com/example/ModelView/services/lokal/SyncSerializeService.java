package com.example.ModelView.services.lokal;

import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.FolderScanRepository;
import com.example.ModelView.services.create.locale.CreateObjService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SyncSerializeService {

    private final FolderScanRepository folderScanRepository;
    private final CreateObjService createObjService;

    public Collection<PrintModelData> getModelForSer() {

        Collection<File> inputSer = folderScanRepository.startScanRepository(false);
        Collection<File> inputFiles = folderScanRepository.startScanRepository(true);

        Set<String> inputSerModelName = getModelName(inputSer);

        Collection<File> inputFileToSave = new ArrayList<>(200);

        inputFiles.stream()
                .filter(file -> !inputSerModelName.contains(file.getParentFile().getName()))
                .forEach(inputFileToSave::add);

        System.out.println("inputFileToSave - " + inputFileToSave.size());
        System.out.println("inputSerModelName - " + inputSerModelName.size());


        return createObjService.startCreateObj(inputFileToSave);
    }

    private Set<String> getModelName(Collection<File> input) {
        Set<String> modelNames = new HashSet<>(5000);
        input.forEach(inputObj -> modelNames.add(inputObj.getName())); // TODO
        return modelNames;
    }
}
