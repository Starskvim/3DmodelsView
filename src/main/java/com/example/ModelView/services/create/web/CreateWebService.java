package com.example.ModelView.services.create.web;

import com.example.ModelView.model.entities.web.PrintModelOthWebData;
import com.example.ModelView.model.entities.web.PrintModelWebData;
import com.example.ModelView.model.rest.PrintModelOthWeb;
import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.model.entities.web.PrintModelTagWebData;
import com.example.ModelView.persistance.repositories.web.ModelRepositoryTagsWeb;
import com.example.ModelView.persistance.repositories.web.ModelRepositoryWeb;
import com.example.ModelView.services.create.EntitiesAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.ModelView.utillity.CreateUtils.detectMyRateForModel;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateWebService {

    private final ModelRepositoryWeb modelRepository;
    private final ModelRepositoryTagsWeb modelRepositoryTags;
    private final EntitiesAttributeService attributeService;

    private static Set<PrintModelWebData> printModelWebDataToSave;
    private static List<PrintModelTagWebData> printModelTagWebDataToSaveList;

    private static Map<String, PrintModelTagWebData> assignTagMap;

    public static Boolean cashReady = false;

    public void addNewModel(PrintModelWeb printModelWebDTO) {

        System.out.println("addNewModel");

        PrintModelWebData printModelWebData = new PrintModelWebData();
        printModelWebData.setModelName(printModelWebDTO.getModelName());
        printModelWebData.setModelSize(printModelWebDTO.getModelSize());
        printModelWebData.setModelCategory(printModelWebDTO.getModelCategory());
        printModelWebData.setModelSize(printModelWebDTO.getModelSize());
        printModelWebData.setModelPath(printModelWebDTO.getModelPath());
        printModelWebData.setViews(0L);
        printModelWebData.setMyRate(detectMyRateForModel(printModelWebDTO.getModelPath())); //TODO ????
        detectAddAndCreateTags(printModelWebDTO, printModelWebData);
        addOthObj(printModelWebDTO, printModelWebData);
        printModelWebDataToSave.add(printModelWebData);
        saveNewModel();
    }


    @Transactional
    public void detectAddAndCreateTags(PrintModelWeb printModelWebDTO, PrintModelWebData printModelWebData) {

        if (!cashReady) {
            prepareDetectTags();
            cashReady = true;
        }

        Collection<String> tagsDTO = printModelWebDTO.getModelTagsNames();

        log.info("tagsDTO ---" + tagsDTO.size() + "---" + printModelWebDTO.getModelTagsNames().toString());


        for (String tag : tagsDTO) {

            if (assignTagMap.containsKey(tag)) {
                PrintModelTagWebData currentTag = assignTagMap.get(tag);
                currentTag.getPrintModels().add(printModelWebData);

//                    Collection<PrintModelWeb> models = currentTag.getPrintModels();
//                    models.add(printModelWeb);

                printModelWebData.getModelTags().add(currentTag);
                printModelTagWebDataToSaveList.add(currentTag);
            } else {
                PrintModelTagWebData tagObj = new PrintModelTagWebData();
                tagObj.setNameTag(tag);
                tagObj.getPrintModels().add(printModelWebData);
                printModelWebData.getModelTags().add(tagObj);
                tagObj.setCountModels(tagObj.getCountModels() + 1);
                printModelTagWebDataToSaveList.add(tagObj);
                assignTagMap.put(tag, tagObj);
            }

        }
    }

    public void prepareDetectTags() {
        printModelWebDataToSave = new HashSet<>();
        printModelTagWebDataToSaveList = new ArrayList<>();
        assignTagMap = new HashMap<>();

        System.out.println("prepareDetectTags new ArrayList()");
        Collection<PrintModelTagWebData> printModelTagWebDataSavedList = new ArrayList<>(modelRepositoryTags.findAll());

        if(printModelTagWebDataSavedList.size() != 0) {
            for (PrintModelTagWebData tag : printModelTagWebDataSavedList) {
                if (tag.getNameTag() != null) {
                    assignTagMap.put(tag.getNameTag(), tag);
                    System.out.println("assignTagMap.put " + tag.getNameTag());
                }
            }
        }

//        assignTagMap = printModelTagWebSavedSet.stream()
//                .collect(Collectors.toConcurrentMap(PrintModelTagWeb::getNameTag, Function.identity()));

        System.out.println("printModelTagWebSavedSet - size " + assignTagMap.size());
    }

    private void addOthObj(PrintModelWeb printModelWebDTO, PrintModelWebData printModelWebData) {

        Collection<PrintModelOthWeb> inputOthList = printModelWebDTO.getModelOthList();

        for (PrintModelOthWeb othWebDTO : inputOthList) {
            PrintModelOthWebData newOth = new PrintModelOthWebData();
            newOth.setOthName(othWebDTO.getNameModelOTH());
            newOth.setOthSize(othWebDTO.getSizeOTH());
            newOth.setOthPreview(othWebDTO.getPreviewOth());
            newOth.setOthFormat(othWebDTO.getModelOTHFormat());
            printModelWebData.getModelOthList().add(newOth);
        }

        if (!printModelWebData.getModelOthList().isEmpty()) {
            printModelWebData.setPreviewModel(printModelWebData.getModelOthList().get(0));
        }
    }

    public void saveNewModel() {
        modelRepositoryTags.saveAll(printModelTagWebDataToSaveList);
        printModelTagWebDataToSaveList.clear();
//        modelRepositoryJPA.saveAll(printModelWebToSaveList);
    }

}
