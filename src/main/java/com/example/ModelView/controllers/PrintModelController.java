package com.example.ModelView.controllers;

import com.example.ModelView.dto.ModelOTHDto;
import com.example.ModelView.dto.PrintModelDto;
import com.example.ModelView.dto.PrintModelPreviewDto;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.specifications.ModelSpecs;
import com.example.ModelView.services.*;
import com.example.ModelView.services.create.CreateDtoService;
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
    private final CreateDtoService createDTOService;

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
            filters.append("@word-").append(wordCategory);
        }

        long start1 = System.currentTimeMillis();
        Page<PrintModel> modelsPages = printModelService.findAllModelByPageAndSpecsService(spec, pageable);
        long fin1 = System.currentTimeMillis();
        System.out.println("Create selects PrintModel " + pageable.getPageNumber() + " Time " + (fin1 - start1));

        long start2 = System.currentTimeMillis();
        List<PrintModelPreviewDto> resultList = createDTOService.createDTOListThreads(modelsPages);
        long fin2 = System.currentTimeMillis();
        System.out.println("Create page " + pageable.getPageNumber() + " Time " + (fin2 - start2));

        long start3 = System.currentTimeMillis();
        List<String> modelTagList = printModelService.getAllTagsName();
        long fin3 = System.currentTimeMillis();
        System.out.println("Create page modelTagList " + pageable.getPageNumber() + " Time " + (fin3 - start3));

        model.addAttribute("modelTagList", modelTagList);

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

    @GetMapping("/modelsByTag")
    public String showTagPage (Model model, Pageable pageable, @RequestParam(value = "tag") String tag){


        System.out.println(tag);

        Page<PrintModel> modelsPages = printModelService.getAllModelByTagService(tag, pageable);

        long start2 = System.currentTimeMillis();
        List<PrintModelPreviewDto> resultList = createDTOService.createDTOListThreads(modelsPages);
        long fin2 = System.currentTimeMillis();
        System.out.println("Create page " + pageable.getPageNumber() + " Time " + (fin2 - start2));

        model.addAttribute("tag", tag);
        model.addAttribute("models", resultList);
        model.addAttribute("allPage", modelsPages.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("pageNumbers",
                printModelService.preparePageIntService(pageable.getPageNumber(), modelsPages.getTotalPages()));

        return "modelsByTag";
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
        PrintModelDto printModelDto = printModelService.createDto(printModel);
        Collection<ModelOTH> printModelOTHList = printModel.getModelOTHSet();
        Collection<ModelZIP> printModelZIPList = printModel.getModelZIPSet();

        Collection<ModelOTHDto> resultListOTH = createDTOService.prepareOTHListDTOService(printModelOTHList);

        model.addAttribute("printModelOTHList", resultListOTH);
        model.addAttribute("printModelZIPList", printModelZIPList);
        model.addAttribute("printModel", printModelDto);
        return "modelPage";
    }

    @PostMapping("/modelOBJ/{id}/delete")
    public String deleteModel (@PathVariable(value = "id") Long id){
        printModelService.deleteModelById(id);
        System.out.println("delete model with id - " + id);
        return "redirect:/models";
    }

    @GetMapping("/modelOBJ/{id}/postOnWeb")
    public String postModelOnWeb (@PathVariable(value = "id") Long id){
        System.out.println("post - " + id);
        printModelService.postModelOnWeb(id);

        return "redirect:/models/modelOBJ/" + id;
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
