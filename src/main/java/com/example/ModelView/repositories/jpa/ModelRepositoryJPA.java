package com.example.ModelView.repositories.jpa;

import com.example.ModelView.entities.PrintModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface ModelRepositoryJPA extends JpaRepository <PrintModel, Long>, JpaSpecificationExecutor<PrintModel> {

    // actual
    @EntityGraph(value = "PrintModel-all", type = EntityGraph.EntityGraphType.LOAD)
    Optional<PrintModel> findById(Long id);

    @EntityGraph(value = "PrintModel-all", type = EntityGraph.EntityGraphType.LOAD)
    List<PrintModel> findAllByModelName(ArrayList<String> modelsNames);

    @EntityGraph(value = "PrintModel-oth", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModel> findAll(Specification<PrintModel> modelSpecification, Pageable pageable);

    void deleteAllByModelNameIn(Collection<String> deletModelSet);

    @EntityGraph(value = "PrintModel-oth", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModel> findAllByModelTagsObj_TagContaining(String nameTag, Pageable pageable);

    // old
    @EntityGraph(value = "PrintModel-oth", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModel> findAllBymodelNameLikeIgnoreCase(String name, Pageable page);

    @Query("Select modelName from PrintModel")
    List<String> getAllNameModel();

    PrintModel getByModelName(String nameModel);




}
