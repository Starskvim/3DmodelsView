package com.example.ModelView.controllers;

import com.example.ModelView.DTO.MapperDTO;
import com.example.ModelView.DTO.ModelOTHDTO;
import com.example.ModelView.DTO.PrintModelDTO;
import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.repositories.specifications.ModelSpecs;
import com.example.ModelView.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class PrintModelController {
    private final PrintModelService printModelService;
    private final CreateObjService createObjService;
    private final CreateSyncObjService createSyncObjService;
    private final MapperDTO mapperDTO;
    private final CreateDTOService createDTOService;

    private static final int INITIAL_PAGE = 0;

    @GetMapping
    public String testshowModelListController(Model model,
                                              @RequestParam(value = "page", required = false) Optional<Integer> page,
                                              @RequestParam(value = "wordName", required = false) String wordName,
                                              @RequestParam(value = "wordCategory", required = false) String wordCategory

    ) {

        final int newCurrentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Specification<PrintModel> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();

        if (wordName != null) {
            spec = spec.and(ModelSpecs.modelNameContains(wordName));
        }
        if (wordCategory != null) {
            spec = spec.and(ModelSpecs.modelCategoryContains(wordCategory));
            filters.append("@word-" + wordCategory);
        }

        Page<PrintModel> modelsPages = printModelService.findAllModelByPageAndSpecsService(newCurrentPage, spec);

        long start = System.currentTimeMillis();
        //List<PrintModelDTO> resultList = createDTOService.createDTOlistThreads(modelsPages);
        List<PrintModelDTO> resultList = createDTOService.createDTOlistStream(modelsPages);
        long fin = System.currentTimeMillis();
        System.out.println("Create page "+ newCurrentPage + " Time " + (fin - start));

        model.addAttribute("models", resultList);

        model.addAttribute("allPage", modelsPages.getTotalPages());
        model.addAttribute("filters", filters.toString());
        model.addAttribute("wordName", wordName);
        model.addAttribute("wordCategory", wordCategory);


        model.addAttribute("page", newCurrentPage);

        model.addAttribute("pageNumbers", preparePageInt(newCurrentPage));
        return "models";
    }


    @GetMapping("/zipPage")
    public String showZIPListController(Model model) {
        model.addAttribute("zips", printModelService.getAllZIPListByPageService(0));
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

            //printModelService.startSyncService();

        } catch (IOException a) {
            System.out.println("IOException");
        }
        long fin = System.currentTimeMillis();
        System.out.println("startSyncController time create - " + (fin - start));
        return "admin";
    }


    @GetMapping("/good")
    public String startGood() {
        return "good";
    }

    @GetMapping("/admin")
    public String startAdmin() {
        return "admin";
    }


    @GetMapping("/modelOBJ/{id}")
    public String showOneModelPage(Model model, @PathVariable(value = "id") Long id) {

        PrintModel printModel = printModelService.getById(id);

        Collection<ModelOTH> printModelOTHList = printModel.getModelOTHList();
        Collection<ModelZIP> printModelZIPList = printModel.getModelZIPList();

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


    public LinkedList<Integer> preparePageInt(int current) {

        LinkedList<Integer> pageNumbers = new LinkedList<>();

        if (current == 2) {
            pageNumbers.add(current);
        } else if (current > 2) {
            pageNumbers.add(current - 1);
            pageNumbers.add(current);
        }

        for (int i = 0; i < 11; i++) {
            current += 1;
            if (!(current == 1)) {
                pageNumbers.add(current);
            }
        }

        return pageNumbers;
    }


}
