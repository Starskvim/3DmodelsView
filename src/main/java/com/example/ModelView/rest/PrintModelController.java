package com.example.ModelView.rest;

import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.rest.request.PrintModelRequest;
import com.example.ModelView.rest.request.PrintModelsPageRequest;
import com.example.ModelView.rest.request.SearchRequestParams;
import com.example.ModelView.rest.specifications.SpecificationBuilder;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.services.*;
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

import static com.example.ModelView.utillity.PageUtils.preparePageIntService;


@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class PrintModelController {

    private final PrintModelService printModelService;
    private final SpecificationBuilder specBuilder;

    @GetMapping
    public String getModels(Model model, Pageable pageable,
                                   @RequestParam(value = "wordName", required = false) String wordName,
                                   @RequestParam(value = "wordCategory", required = false) String wordCategory

    ) {
        SearchRequestParams searchParams = SearchRequestParams.builder()
                .wordName(wordName)
                .wordCategory(wordCategory)
                .build();

        Specification<PrintModelData> searchSpec = specBuilder.createSpec(searchParams);
        PrintModelsPageRequest resultReq = printModelService.getPage(searchSpec, pageable);
        Page result = resultReq.getResult();
        List<String> modelTagList = printModelService.getAllTagsNameWithPage(pageable);

        model.addAttribute("modelTagList", modelTagList);
        model.addAttribute("models", result);
        model.addAttribute("allPage", result.getTotalPages());
//        model.addAttribute("filters", filters.toString());
        model.addAttribute("wordName", wordName);
        model.addAttribute("wordCategory", wordCategory);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("pageNumbers", preparePageIntService(pageable.getPageNumber(), resultReq.getTotalPages()));
        return "models";
    }

    @GetMapping("/tag")
    public String showTagPage(Model model, Pageable pageable, @RequestParam(value = "tag") String tag) {
        Page<PrintModelPreview> result = printModelService.getTagPage(tag, pageable);

        model.addAttribute("tag", tag);
        model.addAttribute("models", result);
        model.addAttribute("allPage", result.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("pageNumbers", preparePageIntService(pageable.getPageNumber(), result.getTotalPages()));
        return "modelsByTag";
    }

    @GetMapping("/zips")
    public String showZipList(Model model, Pageable pageable) {
        List<PrintModelZipData> zipsPages = printModelService.getAllZipsListByPageService(pageable).getContent();

        model.addAttribute("zips", zipsPages);
        return "zipPage";
    }

    @GetMapping("/tags")
    public String showTagsListController(Model model) {
        List<String> modelTagList = printModelService.getAllTagsName();

        model.addAttribute("modelTagList", modelTagList);
        return "tagsPage";
    }

    @GetMapping("/model-obj/{id}")
    public String showOneModelPage(Model model, @PathVariable(value = "id") Long id) {
        PrintModelRequest result = printModelService.getOneModelForPage(id);

        model.addAttribute("printModelOthList", result.getPrintOths());
        model.addAttribute("printModelZipList", result.getPrintZips());
        model.addAttribute("printModel", result.getPrintModel());
        return "modelPage";
    }

    // TODO delete
    @PostMapping("/model-obj/{id}/delete")
    public String deleteModel(@PathVariable(value = "id") Long id) {
        printModelService.deleteModelById(id);
        System.out.println("delete model with id - " + id);
        return "redirect:/models";
    }

    @GetMapping("/model-obj/{id}/post-on-web")
    public String postModelOnWeb(@PathVariable(value = "id") Long id) {
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

    @GetMapping("/good")
    public String startGood() {
        return "good";
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
