package com.example.ModelView.rest;

import com.example.ModelView.services.PrintModelService;
import com.example.ModelView.services.WebSyncService;
import com.example.ModelView.services.create.locale.CreateObjService;
import com.example.ModelView.services.lokal.SerializeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CreateObjService createObjService;
    private final PrintModelService printModelService;
    private final SerializeService serializeService;
    private final WebSyncService webSyncService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/models";
    }

    @GetMapping("/admin")
    public String toAdmin() {
        return "admin";
    }

    @GetMapping("/admin/start")
    public String startSkanController() {
        try {
            printModelService.startFolderScanService();
        } catch (IOException a) {
            return "redirect:/models";
        }
        return "redirect:/models";
    }

    @GetMapping("/admin/startCreate")
    public String startCreateController() {
        long start = System.currentTimeMillis();
        createObjService.startCreateOBJService();
        long fin = System.currentTimeMillis();
        System.out.println("startCreateController time create - " + (fin - start));
        return "redirect:/models";
    }

    @GetMapping("/admin/syncFolder")
    public String startSyncFolderController() {
        long start = System.currentTimeMillis();
        printModelService.startSyncFolderService();
        long fin = System.currentTimeMillis();
        System.out.println("startSyncFolderController time sync - " + (fin - start));
        return "admin";
    }

    @GetMapping("/admin/sync")
    public String startSyncController() {
        long start = System.currentTimeMillis();
        printModelService.startSyncObjService();
        long fin = System.currentTimeMillis();
        System.out.println("startSyncController time create - " + (fin - start));
        return "admin";
    }

    @GetMapping("/admin/syncWeb")
    public String startSyncWebController(){
        long start = System.currentTimeMillis();
        webSyncService.startSyncWeb();
        long fin = System.currentTimeMillis();
        System.out.println("startSyncWeb() time ser - " + (fin - start));
        return "admin";
    }

    @GetMapping("/admin/serialization")
    public String startSerializationController() {
        long start = System.currentTimeMillis();
        serializeService.serializeObj(printModelService.getAllModelListService());
        long fin = System.currentTimeMillis();
        System.out.println("startSerializationController time ser - " + (fin - start));
        return "admin";
    }

    @GetMapping("/admin/serialization/sync")
    public String startSerializationSyncController() {
        long start = System.currentTimeMillis();
        serializeService.serializeObj(printModelService.getSyncSerModelListService());
        long fin = System.currentTimeMillis();
        System.out.println("startSerializationSyncController time create + ser - " + (fin - start));
        return "admin";
    }

    @GetMapping("/admin/deserialization")
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

    @GetMapping("/admin/serialization/{id}")
    public String serializeModel(Model model, @PathVariable(value = "id") Long id) {
        System.out.println("start ser");
        serializeService.serializeOneModelToWebDtoService(id);
        System.out.println("end ser");
        return "redirect:/models/modelOBJ/" + id;
    }

    @RequestMapping(value = "/admin/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        serializeService.handleFileUploadService(file);
        return "redirect:/admin";
    }
}
