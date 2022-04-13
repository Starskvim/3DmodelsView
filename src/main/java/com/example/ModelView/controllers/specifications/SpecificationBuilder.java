package com.example.ModelView.controllers.specifications;

import com.example.ModelView.controllers.request.SearchRequestParams;
import com.example.ModelView.entities.locale.PrintModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecificationBuilder {

    public Specification<PrintModel> createSpec (SearchRequestParams searchRequestParams){

        Specification<PrintModel> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();

        if (searchRequestParams.getWordName() != null) {
            spec = spec.and(ModelSpecs.modelNameContains(searchRequestParams.getWordName()));
        }
        if (searchRequestParams.getWordCategory() != null) {
            spec = spec.and(ModelSpecs.modelCategoryContains(searchRequestParams.getWordCategory()));
            filters.append("@word-").append(searchRequestParams.getWordCategory());
        }

        return spec;
    }

}
