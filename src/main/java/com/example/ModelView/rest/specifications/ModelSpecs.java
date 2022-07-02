package com.example.ModelView.rest.specifications;


import com.example.ModelView.model.entities.locale.PrintModelData;
import org.springframework.data.jpa.domain.Specification;

public class ModelSpecs {
    public static Specification<PrintModelData> modelNameContains (String word){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("modelName")), "%"+word.toUpperCase()+"%");
    }

    public static Specification<PrintModelData> modelCategoryContains (String word){
        return  (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("modelCategory"), "%"+word+"%");
    }

}
