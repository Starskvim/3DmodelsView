package com.example.ModelView.controllers;

import com.example.ModelView.dto.MapperDTO;
import com.example.ModelView.dto.ModelOTHDTO;
import com.example.ModelView.dto.PrintModelDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.specifications.ModelSpecs;
import com.example.ModelView.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;


@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class PrintModelController {
    private final PrintModelService printModelService;
    private final CreateObjService createObjService;
    private final CreateSyncObjService createSyncObjService;
    private final MapperDTO mapperDTO;
    private final CreateDTOService createDTOService;
    private final FolderSyncService folderSyncService;
    private final SerializeService serializeService;

    @GetMapping
    public String modelsController(Model model,
                                              Pageable pageable,
                                              @RequestParam(value = "wordName", required = false) String wordName,
                                              @RequestParam(value = "wordCategory", required = false) String wordCategory

    ) {

        Specification<PrintModel> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();

        if (wordName != null) {
            spec = spec.and(ModelSpecs.modelNameContains(wordName));
        }
        if (wordCategory != null) {
            spec = spec.and(ModelSpecs.modelCategoryContains(wordCategory));
            filters.append("@word-" + wordCategory);
        }


        Page<PrintModel> modelsPages = printModelService.findAllModelByPageAndSpecsService(spec, pageable);

        long start = System.currentTimeMillis();
        List<PrintModelDTO> resultList = createDTOService.createDTOlistThreads(modelsPages);

        long fin = System.currentTimeMillis();
        System.out.println("Create page "+ pageable.getPageNumber() + " Time " + (fin - start));

        model.addAttribute("models", resultList);

        model.addAttribute("allPage", modelsPages.getTotalPages());
        model.addAttribute("filters", filters.toString());
        model.addAttribute("wordName", wordName);
        model.addAttribute("wordCategory", wordCategory);

        model.addAttribute("currentPage", pageable.getPageNumber());

        model.addAttribute("pageNumbers", preparePageInt(pageable.getPageNumber(), modelsPages.getTotalPages()));
        return "models";
    }


    @GetMapping("/zipPage")
    public String showZIPListController(Model model, Pageable pageable) {

        List<ModelZIP> zipsPages = printModelService.getAllZIPListByPageService(pageable).getContent();

        model.addAttribute("zips", zipsPages);
        return "zipPage";
    }

    @GetMapping("/start")
    public String startSkanController() {
        try {
            printModelService.startFolderScanService();
        } catch (IOException a) {
            return "redirect:/models";
        }
        return "redirect:/models";
    }

    @GetMapping("/startCreate")
    public String startCreateController() {
        long start = System.currentTimeMillis();
        try {
            createObjService.startCreateOBJService();

            //printModelService.startFolderCreateService();

        } catch (IOException a) {
            System.out.println("IOException");
        }
        long fin = System.currentTimeMillis();
        System.out.println("startCreateController time create - " + (fin - start));
        return "redirect:/models";
    }

    @GetMapping("/sync")
    public String startSyncController() {
        long start = System.currentTimeMillis();
        try {
            createSyncObjService.startSyncOBJRepository();
        } catch (IOException a) {
            System.out.println("IOException");
        }
        long fin = System.currentTimeMillis();
        System.out.println("startSyncController time create - " + (fin - start));

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "admin";
    }

    @GetMapping("/syncFolder")
    public String startSyncFolderController() {
        long start = System.currentTimeMillis();
        folderSyncService.startSyncFolderService();
        long fin = System.currentTimeMillis();
        System.out.println("startSyncFolderController time sync - " + (fin - start));
        return "admin";
    }

    @GetMapping("/serialization")
    public String startSerializationController() {
        long start = System.currentTimeMillis();
        try {
            serializeService.serializeObj(printModelService.getAllModelListService());
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.currentTimeMillis();
        System.out.println("startSerializationController time ser - " + (fin - start));
        return "admin";
    }

    @GetMapping("/deserialization")
    public String startDeserializationController() {
        long start = System.currentTimeMillis();
        try {
            serializeService.deserializeObj();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        long fin = System.currentTimeMillis();
        System.out.println("startSerializationController time ser - " + (fin - start));
        return "admin";
    }




    @GetMapping("/good")
    public String startGood() {
        return "good";
    }

    @GetMapping("/testProgressBar")
    public String startTestBar() {

        JProgressBarService newProgressBar = new JProgressBarService("testBar", 100);

        int current = 0;

        for (int i = 0; i < 10; i++) {
            current += 10;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            newProgressBar.updateBar(current);
        }

        return "good";
    }

    @GetMapping("/admin")
    public String startAdmin() {
        return "admin";
    }


    @GetMapping("/modelOBJ/{id}")
    public String showOneModelPage(Model model, @PathVariable(value = "id") Long id) {

        PrintModel printModel = printModelService.getById(id);

        Collection<ModelOTH> printModelOTHList = printModel.getModelOTHSet();
        Collection<ModelZIP> printModelZIPList = printModel.getModelZIPSet();

        Collection<ModelOTHDTO> resultListOTH = new ArrayList<>(10);

        for (ModelOTH modelOTH : printModelOTHList) {
            resultListOTH.add(mapperDTO.toModelOTHDTO(modelOTH));
        }

        model.addAttribute("printModelOTHList", resultListOTH);

        model.addAttribute("printModelZIPList", printModelZIPList);
        model.addAttribute("printModel", printModel);


        return "modelPage";
    }

    @PostMapping("/modelPage")
    public String showModelListByPageController(Model model, @ModelAttribute(value = "page") int page) {

        model.addAttribute("models", printModelService.getAllModelListByPageService(page));

        return "models";
    }


    @PostMapping("/search_name")
    public String searchByNameController(Model model, @ModelAttribute(value = "word") String word) {

        model.addAttribute("word", word);
        model.addAttribute("models", printModelService.searchByModelNameService(word, 0));
        return "models";
    }


    @RequestMapping(value = "/open", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void openController(@RequestParam(value = "path") String path) {
        try {
            printModelService.openFolderOrFile(path);
        } catch (IOException a) {
            System.out.println(a + "  -  " + path);
        }
    }


    public ArrayList<Integer> preparePageInt(int current, int totalPages) {

        ArrayList<Integer> pageNumbers = new ArrayList<>();

        pageNumbers.add(0);
        if (current >= 2) {
            pageNumbers.add(current - 1);
            pageNumbers.add(current);
        } else if (current == 1 || current == 0){
            pageNumbers.add(1);
        }
        for (int i = 0; i < 11; i++) {
            current += 1;
            if (current > totalPages - 2){
                for ( i = current; i < totalPages - current; i++){
                    current++;
                    pageNumbers.add(current);
                }
                break;
            }
            if (!(current == 1)) {
                pageNumbers.add(current);
            }
        }
        pageNumbers.add(totalPages);
        return pageNumbers;
    }


}
