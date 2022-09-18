package com.example.ModelView.rest.specifications;

import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.entities.web.PrintModelWebData;
import com.example.ModelView.rest.request.SearchRequestParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecificationBuilder {

    public Specification<PrintModelData> createSpec (SearchRequestParams searchRequestParams){
        Specification<PrintModelData> spec = Specification.where(null);

        if (searchRequestParams.getWordName() != null) {
            spec = spec.and(ModelSpecs.modelNameContains(searchRequestParams.getWordName()));
        }
        if (searchRequestParams.getWordCategory() != null) {
            spec = spec.and(ModelSpecs.modelCategoryContains(searchRequestParams.getWordCategory()));
        }
        return spec;
    }

    public Specification<PrintModelWebData> createWebSpec (SearchRequestParams searchRequestParams){
        Specification<PrintModelWebData> spec = Specification.where(null);

        if (searchRequestParams.getWordName() != null) {
            spec = spec.and(ModelSpecs.modelNameContainsWeb(searchRequestParams.getWordName()));
        }
        if (searchRequestParams.getWordCategory() != null) {
            spec = spec.and(ModelSpecs.modelCategoryContainsWeb(searchRequestParams.getWordCategory()));
        }
        return spec;
    }

}
