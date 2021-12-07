package com.example.ModelView.controllers;

import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelZIP;
import com.example.ModelView.entities.PrintModel;
import com.example.ModelView.services.CreateObjService;
import com.example.ModelView.services.CreateSyncObjService;
import com.example.ModelView.services.PrintModelService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.catalina.util.URLEncoder;

@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class PrintModelController {
    private final PrintModelService printModelService;
    private final CreateObjService createObjService;
    private final CreateSyncObjService createSyncObjService;

    @GetMapping
    public String showModelListController(Model model){
        model.addAttribute("models", printModelService.getAllModelListByPageService(0));
        return "models";
    }

    @GetMapping("/zipPage")
    public String showZIPListController(Model model){

        model.addAttribute("zips", printModelService.getAllZIPListByPageService(0));

        return "zipPage";
    }

    @GetMapping("/start")
    public String startSkanController(){
        try {
            printModelService.startFolderScanService();
        } catch (IOException a) {
            return "redirect:/models";
        }
        return "redirect:/models";
    }

    @GetMapping("/startCreate")
    public String startCreateController(){
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
    public String startSyncController(){
        long start = System.currentTimeMillis();
        try {
            createSyncObjService.startSyncOBJRepository();

            //printModelService.startSyncService();

        } catch (IOException a) {
            System.out.println("IOException");
        }
        long fin = System.currentTimeMillis();
        System.out.println("startSyncController time create - " + (fin - start));
        return "redirect:/models/admin";
    }



    @GetMapping("/good")
    public String startGood(){
        return "good";
    }

    @GetMapping("/admin")
    public String startAdmin(){
        return "admin";
    }



    @GetMapping("/modelOBJ/{id}")
    public String showOneModelPage(Model model, @PathVariable(value = "id") Long id){

        PrintModel printModel = printModelService.getById(id);
        Collection<ModelOTH> printModelOTHList = printModel.getModelOTHList();
        Collection<ModelZIP> printModelZIPList = printModel.getModelZIPList();

        model.addAttribute("printModelOTHList", printModelOTHList);
        model.addAttribute("printModelZIPList", printModelZIPList);
        model.addAttribute("printModel", printModel);

        return "modelPage";
    }

    @PostMapping("/modelPage")
    public String showModelListByPageController(Model model, @ModelAttribute(value = "page") int page){
        model.addAttribute("models", printModelService.getAllModelListByPageService(page));
        return "models";
    }

    @PostMapping("/search_name")
    public String searchByNameController(Model model, @ModelAttribute(value = "word") String word){

        model.addAttribute("models", printModelService.searchByModelNameService(word, 0));
        return "models";
    }


    @RequestMapping(value = "/open", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void openController(@RequestParam (value = "path") String path){
        try {
            printModelService.openFolderOrFile(path);
        } catch (IOException a) {
            System.out.println(a + "  -  " + path);
        }
    }




}
