package com.example.ModelView.services;

import com.example.ModelView.mapping.OldPrintModelMapper;
import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.entities.locale.PrintModelOthData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.rest.PrintModelOth;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.persistance.PrintModelDataService;
import com.example.ModelView.rest.exceptions.ModelNotFoundException;
import com.example.ModelView.mapping.MapperDto;
import com.example.ModelView.model.rest.PrintModel;
import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.rest.request.PrintModelRequest;
import com.example.ModelView.rest.request.PrintModelsPageRequest;
import com.example.ModelView.services.create.locale.CreateDtoService;
import com.example.ModelView.services.create.locale.PrintModelLocalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.ModelView.utillity.Constant.Log.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PrintModelService {

    private final PrintModelLocalService localService;
    private final PrintModelDataService dataService;
    private final CreateDtoService createDTOService;
    private final WebRestService webRestService;
    private final MapperDto mapperDto;
    private final OldPrintModelMapper oldPrintModelMapper;

    public PrintModelsPageRequest getPage(Specification<PrintModelData> searchSpec, Pageable pageable) {

        long start1 = System.currentTimeMillis();
        Page<PrintModelData> modelsPage = dataService.findAllWithSpecs(searchSpec, pageable);
        long fin1 = System.currentTimeMillis();
        log.info(CREATE_SELECT,  pageable.getPageNumber(), (fin1 - start1));

        long start2 = System.currentTimeMillis();
        List<PrintModelPreview> resultList = createDTOService.createDTOListThreads(modelsPage);
        long fin2 = System.currentTimeMillis();
        log.info(CREATE_PAGE, pageable.getPageNumber(), (fin2 - start2));

        PageImpl<PrintModelPreview> page = new PageImpl<>(resultList, pageable, modelsPage.getTotalElements());
        return new PrintModelsPageRequest(modelsPage.getTotalPages(), page); // TODO ????
    }

    public PrintModelRequest getOneModelForPage(Long id) {
        PrintModelData printModelData = getById(id);
        PrintModel printModel = createDto(printModelData);
        Collection<PrintModelOthData> printPrintModelOthDataList = printModelData.getPrintModelOthDataSet();
        Collection<PrintModelZipData> printPrintModelZipDataList = printModelData.getPrintModelZipDataSet();
        Collection<PrintModelOth> resultListOTH = createDTOService.prepareOTHListDTOService(printPrintModelOthDataList);
        return new PrintModelRequest(printModel, resultListOTH, printPrintModelZipDataList);
    }

    public List<String> getAllTagsNameWithPage(Pageable pageable) {
        long start3 = System.currentTimeMillis();
        List<String> modelTagList = dataService.getAllNameTags();
        long fin3 = System.currentTimeMillis();
        log.info(CREATE_PAGE_TAG, pageable.getPageNumber(), (fin3 - start3));
        return modelTagList;
    }

    public List<String> getAllTagsName() {
        return dataService.getAllNameTags();
    }

    public Page<PrintModelPreview> getTagPage(String tag, Pageable pageable) {

        Page<PrintModelData> modelsPages = dataService.findAllByModelTagsObj_TagContaining(tag, pageable);
        long start2 = System.currentTimeMillis();
        List<PrintModelPreview> resultList = createDTOService.createDTOListThreads(modelsPages);
        long fin2 = System.currentTimeMillis();
        log.info(CREATE_PAGE, pageable.getPageNumber(), (fin2 - start2));

        return new PageImpl<>(resultList, pageable, resultList.size());
    }

    public List<PrintModelData> getAllModelList() {
        return dataService.findAll();
    }

    public Collection<PrintModelData> getSyncSerModelList() {
        return localService.getModelForSer();
    }

    public Page<PrintModelZipData> getAllZipsListByPageService(Pageable pageable) {
        return dataService.findAllZips(pageable);
    }

    public void startFolderScanService() throws IOException {
        localService.startScanRepository(true);
    }

    public PrintModelData getById(Long id) {
        Optional<PrintModelData> printModel = dataService.findById(id);
        return printModel.orElseThrow(() -> new ModelNotFoundException(id));
    }

    @Transactional
    public void deleteModelById(Long id) {
        dataService.deleteById(id);
    }

    public void openFolderOrFile(String address) throws IOException {
        localService.openFolderOrFile(address);
    }

    public PrintModel createDto(PrintModelData printModelData) {
        return oldPrintModelMapper.toPrintModelDto(printModelData);
    }

    public void postModelOnWeb(Long id) {
        PrintModelData printModelData = getById(id);
        log.info(POST_GET, printModelData.getModelName());
        webRestService.createPostModel(mapperDto.toPrintModelWebDto(printModelData));
    }

    public void postSyncModelOnWeb(PrintModelData printModelData) {
        log.info(POST_SYNC_GET, printModelData.getModelName());
        webRestService.createPostModel(mapperDto.toPrintModelWebDto(printModelData));
    }

    public void postSyncModelOnWeb(PrintModelWeb printModel) {
        log.info(POST_SYNC_GET, printModel.getModelName());
        if (Objects.isNull(printModel)) {
            return;
        }
        webRestService.createPostModel(printModel);
    }

    public void startSyncFolderService() {
        localService.startSyncFolderService();
    }

    public void startSyncObjService() {
        localService.startSyncOBJRepository();
    }
}
