package com.example.ModelView.services;

import com.example.ModelView.mapping.PrintModelMapper;
import com.example.ModelView.mapping.PrintModelOthMapper;
import com.example.ModelView.model.entities.web.PrintModelOthWebData;
import com.example.ModelView.model.entities.web.PrintModelWebData;
import com.example.ModelView.model.rest.PrintModel;
import com.example.ModelView.model.rest.PrintModelOth;
import com.example.ModelView.model.rest.PrintModelPreview;
import com.example.ModelView.model.rest.PrintModelWeb;
import com.example.ModelView.persistance.WebPrintModelDataService;
import com.example.ModelView.rest.exceptions.ModelNotFoundException;
import com.example.ModelView.rest.request.PrintModelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebPrintModelService {

    private final WebPrintModelDataService dataService;
    private final WebRestService webRestService;
    private final PrintModelMapper printModelMapper;
    private final PrintModelOthMapper printOthMapper;

    @Transactional(readOnly = true)
    public Page<PrintModelPreview> getPage(Specification<PrintModelWebData> searchSpec, Pageable pageable) {

        long start1 = System.currentTimeMillis();
        Page<PrintModelWebData> modelsPage = dataService.findAllWithSpecs(searchSpec, pageable);
        long fin1 = System.currentTimeMillis();
        System.out.println("Create selects PrintModel " + pageable.getPageNumber() + " Time " + (fin1 - start1));

        long start2 = System.currentTimeMillis();
        List<PrintModelPreview> resultList = printModelMapper.dataToApiPreview(modelsPage.getContent());
        long fin2 = System.currentTimeMillis();
        System.out.println("Create page " + pageable.getPageNumber() + " Time " + (fin2 - start2));

        return new PageImpl<>(resultList, pageable, resultList.size());

    }

    public PrintModelRequest<PrintModel> getOneModelForPage(Long id) {
        PrintModelWebData printModelData = getById(id);

        PrintModel printModel = printModelMapper.dataToApi(printModelData);
        List<PrintModelOthWebData> printPrintModelOthDataList = printModelData.getModelOthList();
        Collection<PrintModelOth> resultListOTH = printOthMapper.dataToApi(printPrintModelOthDataList);

        return new PrintModelRequest(printModel, null, resultListOTH);
    }

    @Transactional(readOnly = true)
    public List<String> getAllTagsNameWithPage(Pageable pageable) {
        long start3 = System.currentTimeMillis();
        List<String> modelTagList = dataService.getAllNameTags();
        long fin3 = System.currentTimeMillis();
        System.out.println("Create page modelTagList " + pageable.getPageNumber() + " Time " + (fin3 - start3));
        return modelTagList;
    }

    @Transactional(readOnly = true)
    public Page<PrintModelPreview> getTagPage(String tag, Pageable pageable) {
        Page<PrintModelWebData> modelsPages = dataService.findAllByModelTagsObj_TagContaining(tag, pageable);

//        long start2 = System.currentTimeMillis();
//        List<PrintModelPreview> resultList = printModelMapper.dataToApiFull(modelsPages);
//        long fin2 = System.currentTimeMillis();
//        System.out.println("Create page " + pageable.getPageNumber() + " Time " + (fin2 - start2));
//        return new PageImpl<>(resultList, pageable, resultList.size());

        long start2 = System.currentTimeMillis();
        List<PrintModelPreview> resultList = printModelMapper.dataToApiPreview(modelsPages.getContent());
        long fin2 = System.currentTimeMillis();
        System.out.println("Create page " + pageable.getPageNumber() + " Time " + (fin2 - start2));

        return new PageImpl<>(resultList, pageable, resultList.size());
    }

    @Transactional(readOnly = true)
    public List<String> getAllTagsName() {
        return dataService.getAllNameTags();
    }

    @Transactional
    public void deleteModelById(Long id) {
        dataService.deleteById(id);
    }

    public void postModelOnWeb(Long id) {
        PrintModelWebData printModelData = getById(id);
        System.out.println("post get - " + printModelData.getModelName());
        webRestService.createPostModel(printModelMapper.dataToApiFull(printModelData));
    }

    public PrintModelWebData getById(Long id) {
        Optional<PrintModelWebData> printModel = dataService.findById(id);
        return printModel.orElseThrow(() -> new ModelNotFoundException(id));
    }
}
