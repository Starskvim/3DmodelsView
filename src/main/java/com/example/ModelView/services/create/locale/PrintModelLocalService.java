package com.example.ModelView.services.create.locale;

import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.persistance.FolderScanRepository;
import com.example.ModelView.services.lokal.FolderSyncService;
import com.example.ModelView.services.lokal.SyncSerializeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PrintModelLocalService {

    private final FolderScanRepository folderScanRepository;
    private final FolderSyncService folderSyncService;
    private final SyncSerializeService syncSerializeService;
    private final CreateSyncObjService createSyncObjService;

    public Collection<PrintModelData> getModelForSer() {
        return syncSerializeService.getModelForSer();
    }

    public void startScanRepository(boolean b) {
        folderScanRepository.startScanRepository(b);
    }

    public void startSyncFolderService() {
        folderSyncService.startSyncFolderService();
    }

    public void startSyncOBJRepository() {
        createSyncObjService.startSyncOBJRepository();
    }

    public void openFolderOrFile(String address) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select," + address);
    }
}
