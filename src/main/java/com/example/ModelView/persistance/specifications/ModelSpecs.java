package com.example.ModelView.persistance.specifications;


import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.entities.web.PrintModelWebData;
import org.springframework.data.jpa.domain.Specification;

public class ModelSpecs {

    public static Specification<PrintModelData> modelNameContains (String word){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("modelName")), "%"+word.toUpperCase()+"%");
    }

    public static Specification<PrintModelData> modelCategoryContains (String word){
        return  (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("modelCategory"), "%"+word+"%");
    }

    public static Specification<PrintModelWebData> modelNameContainsWeb (String word){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("modelName")), "%"+word.toUpperCase()+"%");
    }

    public static Specification<PrintModelWebData> modelCategoryContainsWeb (String word){
        return  (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("modelCategory"), "%"+word+"%");
    }

}
