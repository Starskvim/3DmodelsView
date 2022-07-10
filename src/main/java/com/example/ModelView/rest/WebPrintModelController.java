package com.example.ModelView.rest;

import com.example.ModelView.model.entities.web.PrintModelWebData;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.rest.request.PrintModelRequest;
import com.example.ModelView.rest.request.SearchRequestParams;
import com.example.ModelView.rest.specifications.SpecificationBuilder;
import com.example.ModelView.services.WebPrintModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ModelView.utillity.PageUtils.preparePageIntService;

@Controller
@RequestMapping("/models/web")
@RequiredArgsConstructor
public class WebPrintModelController {

    private final WebPrintModelService printModelService;
    private final SpecificationBuilder specBuilder;

    @GetMapping
    public String modelsController(Model model, Pageable pageable,
                                   @RequestParam(value = "wordName", required = false) String wordName,
                                   @RequestParam(value = "wordCategory", required = false) String wordCategory

    ) {
        SearchRequestParams searchParams = SearchRequestParams.builder()
                .wordName(wordName)
                .wordCategory(wordCategory)
                .build();

        Specification<PrintModelWebData> searchSpec = specBuilder.createWebSpec(searchParams);
        Page<PrintModelPreview> result = printModelService.getPage(searchSpec, pageable);
        List<String> modelTagList = printModelService.getAllTagsNameWithPage(pageable);


        model.addAttribute("modelTagList", modelTagList);
        model.addAttribute("models", result);
        model.addAttribute("allPage", result.getTotalPages());
        model.addAttribute("wordName", wordName);
        model.addAttribute("wordCategory", wordCategory);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("pageNumbers", preparePageIntService(pageable.getPageNumber(), result.getTotalPages()));
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

    @GetMapping("/tags")
    public String showTagsListController(Model model) {
        List<String> modelTagList = printModelService.getAllTagsName();

        model.addAttribute("modelTagList", modelTagList);
        return "tagsPage";
    }

    @GetMapping("/model-obj/{id}")
    public String showOneModelPage(Model model, @PathVariable(value = "id") Long id) {

        PrintModelRequest result = printModelService.getOneModelForPage(id);

        model.addAttribute("printModelOTHList", result.getPrintOths());
        model.addAttribute("printModelZIPList", result.getPrintZips());
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

    @GetMapping("/model-obj/{id}/postOnWeb")
    public String postModelOnWeb(@PathVariable(value = "id") Long id) {
        System.out.println("post - " + id);
        printModelService.postModelOnWeb(id);

        return "redirect:/models/modelOBJ/" + id;
    }

}
