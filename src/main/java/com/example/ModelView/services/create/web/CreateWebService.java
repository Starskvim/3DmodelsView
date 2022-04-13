package com.example.ModelView.services.create.web;

import com.example.ModelView.dto.web.PrintModelOTHWebDTO;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import com.example.ModelView.entities.web.PrintModelOthWeb;
import com.example.ModelView.entities.web.PrintModelTagWeb;
import com.example.ModelView.entities.web.PrintModelWeb;
import com.example.ModelView.repositories.jpa.web.ModelRepositoryTagsWeb;
import com.example.ModelView.repositories.jpa.web.ModelRepositoryWeb;
import com.example.ModelView.services.create.EntitiesAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateWebService {

    private final ModelRepositoryWeb modelRepositoryJPA;
    private final ModelRepositoryTagsWeb modelRepositoryTagsJPA;
    private final EntitiesAttributeService attributeService;

    private static Set<PrintModelWeb> printModelWebToSave;
    private static List<PrintModelTagWeb> printModelTagWebToSaveList;

    private static Map<String, PrintModelTagWeb> assignTagMap;

    public static Boolean cashReady = false;

    public void addNewModel(PrintModelWebDTO printModelWebDTO) {

        System.out.println("addNewModel");

        PrintModelWeb printModelWeb = new PrintModelWeb();
        printModelWeb.setModelName(printModelWebDTO.getModelName());
        printModelWeb.setModelSize(printModelWebDTO.getModelSize());
        printModelWeb.setModelCategory(printModelWebDTO.getModelCategory());
        printModelWeb.setModelSize(printModelWebDTO.getModelSize());
        printModelWeb.setModelPath(printModelWebDTO.getModelPath());
        printModelWeb.setViews(0L);
        printModelWeb.setMyRate(EntitiesAttributeService.detectMyRateForModel(printModelWebDTO.getModelPath())); //TODO ????
        detectAddAndCreateTags(printModelWebDTO, printModelWeb);
        addOthObj(printModelWebDTO, printModelWeb);
        printModelWebToSave.add(printModelWeb);
        saveNewModel();
    }


    @Transactional
    public void detectAddAndCreateTags(PrintModelWebDTO printModelWebDTO, PrintModelWeb printModelWeb) {

        if (!cashReady) {
            prepareDetectTags();
            cashReady = true;
        }

        Collection<String> tagsDTO = printModelWebDTO.getModelTagsNames();

        log.info("tagsDTO ---" + tagsDTO.size() + "---" + printModelWebDTO.getModelTagsNames().toString());


        for (String tag : tagsDTO) {

            if (assignTagMap.containsKey(tag)) {
                PrintModelTagWeb currentTag = assignTagMap.get(tag);

                currentTag.getPrintModels().add(printModelWeb);

//                    Collection<PrintModelWeb> models = currentTag.getPrintModels();
//                    models.add(printModelWeb);

                printModelWeb.getModelTags().add(currentTag);
                printModelTagWebToSaveList.add(currentTag);
            } else {
                PrintModelTagWeb tagObj = new PrintModelTagWeb();
                tagObj.setNameTag(tag);
                tagObj.getPrintModels().add(printModelWeb);
                printModelWeb.getModelTags().add(tagObj);
                tagObj.setCountModels(tagObj.getCountModels() + 1);
                printModelTagWebToSaveList.add(tagObj);
                assignTagMap.put(tag, tagObj);
            }

        }
    }

    public void prepareDetectTags() {
        printModelWebToSave = new HashSet<>();
        printModelTagWebToSaveList = new ArrayList<>();
        assignTagMap = new HashMap<>();

        System.out.println("prepareDetectTags new ArrayList()");
        Collection<PrintModelTagWeb> printModelTagWebSavedList = new ArrayList<>(modelRepositoryTagsJPA.findAll());

        if(printModelTagWebSavedList.size() != 0) {
            for (PrintModelTagWeb tag : printModelTagWebSavedList) {
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

    private void addOthObj(PrintModelWebDTO printModelWebDTO, PrintModelWeb printModelWeb) {

        Collection<PrintModelOTHWebDTO> inputOthList = printModelWebDTO.getModelOTHList();

        for (PrintModelOTHWebDTO othWebDTO : inputOthList) {
            PrintModelOthWeb newOth = new PrintModelOthWeb();
            newOth.setOthName(othWebDTO.getNameModelOTH());
            newOth.setOthSize(othWebDTO.getSizeOTH());
            newOth.setPreviewOth(othWebDTO.getPreviewOth());
            newOth.setOthFormat(othWebDTO.getModelOTHFormat());
            printModelWeb.getModelOthList().add(newOth);
        }

        if (!printModelWeb.getModelOthList().isEmpty()) {
            printModelWeb.setPreviewModel(printModelWeb.getModelOthList().get(0));
        }
    }

    public void saveNewModel() {
        modelRepositoryTagsJPA.saveAll(printModelTagWebToSaveList);
        printModelTagWebToSaveList.clear();
//        modelRepositoryJPA.saveAll(printModelWebToSaveList);
    }

}
