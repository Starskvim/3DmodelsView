package com.example.ModelView.services.create;

import com.example.ModelView.model.entities.locale.PrintModelTagData;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryTags;
import com.example.ModelView.services.create.locale.CollectionsService;
import lombok.*;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntitiesAttributeService {

    private final CollectionsService collectionsService;
    private final ModelRepositoryTags modelRepositoryTags;

    //TODO need obj
    private static volatile Boolean isSevenZipInitialized = false;
    private CopyOnWriteArraySet<PrintModelTagData> modelsTagsToSaveSet;
    private HashSet<PrintModelTagData> modelsTagsSavedSet;

    private ConcurrentMap<String, PrintModelTagData> assignTagMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void postConstruct() {
        modelsTagsToSaveSet = collectionsService.getModelsTagsToSaveSet();
        modelsTagsSavedSet = collectionsService.getModelsTagsSavedSet();
    }

    public static void initSevenZip()
    {
        synchronized (isSevenZipInitialized)
        {
            boolean inited = isSevenZipInitialized;
            if (inited)
                return;
            try
            {
                SevenZip.initSevenZipFromPlatformJAR();
                isSevenZipInitialized = true;
                System.out.println("7zip initialized successfully!");
            }
            catch (SevenZipNativeInitializationException s)
            {
                System.out.println("Unable to initialize 7zip! " + s.getMessage());
            }
        }
    }

    public int getCreateArchiveCompressionRatio(String sourceZipFile) {

        initSevenZip();

        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(sourceZipFile, "r");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

            double allsize = 0;
            double allOrigsize = 0;

            for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                try {
                    allsize += item.getSize();
                    allOrigsize += item.getPackedSize();
                } catch (Exception e) {
                    allsize += 0.0;
                    allOrigsize += 0.0;
                }
            }
            double preResult = allOrigsize / allsize;

            //System.out.println(sourceZipFile +" - ok - "+ preResult);

            return (int) Math.round(preResult * 100.0); ////////
        } catch (Exception e) {
            System.err.println("Error occurs: " + e.getMessage() + "-----" + sourceZipFile);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    System.err.println("Error closing archive: " + e.getMessage());
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
        return 0;
    }

    public void prepareDetectTags(){
        modelsTagsSavedSet.addAll(modelRepositoryTags.findAll());
        assignTagMap = modelsTagsSavedSet.stream()
                .collect(Collectors.toConcurrentMap(PrintModelTagData::getTag, Function.identity()));
    }

    public void detectCreateObjTag (String path) {
        String reg = "\\\\";
        String[] splitString = path.split(reg);
        for (String word : splitString) {
            if (word.contains("[") && !word.equals("[3D PRINT]") && !word.equals("[Patreon]")) {
                StringBuilder stringBuilder = new StringBuilder();
                int status = 0;
                for (char ch : word.toCharArray()) {
                    if (ch == '[') {
                        status++;
                    }
                    if(status > 0){
                        stringBuilder.append(ch);
                    }
                    if(ch == ']'){
                        status--;
                    }
                }
                PrintModelTagData printModelTagData = new PrintModelTagData(stringBuilder.toString());
                if(!modelsTagsSavedSet.contains(printModelTagData) && !modelsTagsToSaveSet.contains(printModelTagData)){
                    modelsTagsToSaveSet.add(printModelTagData);
                    assignTagMap.put(printModelTagData.getTag(), printModelTagData);
                }
            }
        }
    }

    public void assignTags (PrintModelData printModelData){
        for (String key : assignTagMap.keySet()) {
            if(printModelData.getModelDirectory().contains(key)){ // TODO <- non optimal
                PrintModelTagData printModelTagData = assignTagMap.get(key);

                List<PrintModelData> printModelsData = printModelTagData.getPrintModelData();
                printModelsData.add(printModelData);

                List<PrintModelTagData> modelTagsObjData = printModelData.getModelTagsObjData();
                modelTagsObjData.add(printModelTagData);

            }
        }

    }
}
