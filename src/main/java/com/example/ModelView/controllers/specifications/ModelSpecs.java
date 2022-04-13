package com.example.ModelView.controllers.specifications;


import com.example.ModelView.entities.locale.PrintModel;
import org.springframework.data.jpa.domain.Specification;

public class ModelSpecs {
    public static Specification<PrintModel> modelNameContains (String word){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("modelName")), "%"+word.toUpperCase()+"%");
    }

    public static Specification<PrintModel> modelCategoryContains (String word){
        return  (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("modelCategory"), "%"+word+"%");
    }

}
