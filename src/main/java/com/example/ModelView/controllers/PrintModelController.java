package com.example.ModelView.controllers;

import com.example.ModelView.dto.ModelOTHDTO;
import com.example.ModelView.dto.PrintModelDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.specifications.ModelSpecs;
import com.example.ModelView.services.*;
import com.example.ModelView.services.create.CreateDTOService;
//import com.example.ModelView.services.lokal.JProgressBarService;
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



@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class PrintModelController {
    private final PrintModelService printModelService;
    private final CreateDTOService createDTOService;

    @GetMapping
    public String modelsController(Model model, Pageable pageable,
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

        long start1 = System.currentTimeMillis();
        Page<PrintModel> modelsPages = printModelService.findAllModelByPageAndSpecsService(spec, pageable);
        long fin1 = System.currentTimeMillis();
        System.out.println("Create selects " + pageable.getPageNumber() + " Time " + (fin1 - start1));

        long start2 = System.currentTimeMillis();
        List<PrintModelDTO> resultList = createDTOService.createDTOListThreads(modelsPages);
        long fin2 = System.currentTimeMillis();
        System.out.println("Create page " + pageable.getPageNumber() + " Time " + (fin2 - start2));

        model.addAttribute("models", resultList);
        model.addAttribute("allPage", modelsPages.getTotalPages());
        model.addAttribute("filters", filters.toString());
        model.addAttribute("wordName", wordName);
        model.addAttribute("wordCategory", wordCategory);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("pageNumbers",
                printModelService.preparePageIntService(pageable.getPageNumber(), modelsPages.getTotalPages()));
        return "models";
    }

    @GetMapping("/zipPage")
    public String showZIPListController(Model model, Pageable pageable) {

        List<ModelZIP> zipsPages = printModelService.getAllZIPListByPageService(pageable).getContent();

        model.addAttribute("zips", zipsPages);
        return "zipPage";
    }

    @GetMapping("/good")
    public String startGood() {
        return "good";
    }

    @GetMapping("/modelOBJ/{id}")
    public String showOneModelPage(Model model, @PathVariable(value = "id") Long id) {
        PrintModel printModel = printModelService.getById(id);
        Collection<ModelOTH> printModelOTHList = printModel.getModelOTHSet();
        Collection<ModelZIP> printModelZIPList = printModel.getModelZIPSet();

        Collection<ModelOTHDTO> resultListOTH = createDTOService.prepareOTHListDTOService(printModelOTHList);

        model.addAttribute("printModelOTHList", resultListOTH);
        model.addAttribute("printModelZIPList", printModelZIPList);
        model.addAttribute("printModel", printModel);
        return "modelPage";
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

    //    @GetMapping("/testProgressBar")
//    public String startTestBar() {
//        JProgressBarService newProgressBar = new JProgressBarService("testBar", 100);
//        int current = 0;
//        for (int i = 0; i < 10; i++) {
//            current += 10;
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            newProgressBar.updateBar(current);
//        }
//        return "good";
//    }

}
