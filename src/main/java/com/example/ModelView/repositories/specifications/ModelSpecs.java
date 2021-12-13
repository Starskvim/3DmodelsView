package com.example.ModelView.repositories.specifications;


import com.example.ModelView.entities.PrintModel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ModelSpecs {
    public static Specification<PrintModel> modelNameContains (String word){
        return new Specification<PrintModel>() {
            @Override
            public Predicate toPredicate(Root<PrintModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("modelName"), "%"+word+"%");
            }
        };
    }

    public static Specification<PrintModel> modelCategoryContains (String word){
        return (Specification<PrintModel>) (root, query, criteriaBuilder) -> {return criteriaBuilder.like(root.get("modelCategory"), "%"+word+"%");};
    }
}
