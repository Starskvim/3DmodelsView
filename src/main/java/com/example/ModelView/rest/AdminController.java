package com.example.ModelView.rest;

import com.example.ModelView.services.PrintModelService;
import com.example.ModelView.services.WebSyncService;
import com.example.ModelView.services.create.locale.CreateObjService;
import com.example.ModelView.services.lokal.SerializeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
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
    public String startScanController() {
        try {
            printModelService.startFolderScanService();
        } catch (IOException a) {
            return "redirect:/models";
        }
        return "redirect:/models";
    }

    @GetMapping("/admin/start-create")
    public String startCreateController() {
        long start = System.currentTimeMillis();
        createObjService.startCreateOBJService();
        long fin = System.currentTimeMillis();
        log.info("startCreateController time create - {}", (fin - start));
        return "redirect:/models";
    }

    @GetMapping("/admin/sync/folders")
    public String startSyncFoldersController() {
        long start = System.currentTimeMillis();
        printModelService.startSyncFolderService();
        long fin = System.currentTimeMillis();
        log.info("startSyncFolderController time sync - {}", (fin - start));
        return "admin";
    }

    @GetMapping("/admin/sync")
    public String startSyncController() {
        long start = System.currentTimeMillis();
        printModelService.startSyncObjService();
        long fin = System.currentTimeMillis();
        log.info("startSyncController time create - {}", (fin - start));
        return "admin";
    }

    @GetMapping("/admin/sync/web")
    public String startSyncWebController(){
        long start = System.currentTimeMillis();
        webSyncService.startSyncWeb();
        long fin = System.currentTimeMillis();
        log.info("startSyncWeb() time ser - {}", (fin - start));
        return "admin";
    }

    @GetMapping("/admin/serialization")
    public String startSerializationController() {
        long start = System.currentTimeMillis();
        serializeService.serializeObj(printModelService.getAllModelList());
        long fin = System.currentTimeMillis();
        log.info("startSerializationController time ser - {}", (fin - start));
        return "admin";
    }

    @GetMapping("/admin/serialization/sync")
    public String startSerializationSyncController() {
        long start = System.currentTimeMillis();
        serializeService.serializeObj(printModelService.getSyncSerModelList());
        long fin = System.currentTimeMillis();
        log.info("startSerializationSyncController time create + ser - {}", (fin - start));
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
        log.info("startSerializationController time ser - {}", (fin - start));
        return "admin";
    }

    @GetMapping("/admin/serialization/{id}")
    public String serializeModel(Model model, @PathVariable(value = "id") Long id) {
        log.info("start ser");
        serializeService.serializeOneModelToWebDtoService(id);
        log.info("end ser");
        System.out.println("end ser");
        return "redirect:/models/modelOBJ/" + id;
    }
}
