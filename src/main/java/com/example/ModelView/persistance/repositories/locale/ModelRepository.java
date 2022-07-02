package com.example.ModelView.persistance.repositories.locale;

import com.example.ModelView.model.entities.locale.PrintModelData;
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


public interface ModelRepository extends JpaRepository <PrintModelData, Long>, JpaSpecificationExecutor<PrintModelData> {

    // actual
    @EntityGraph(value = "PrintModel-all", type = EntityGraph.EntityGraphType.LOAD)
    Optional<PrintModelData> findById(Long id);

    @EntityGraph(value = "PrintModel-all", type = EntityGraph.EntityGraphType.LOAD)
    List<PrintModelData> findAllByModelNameIn(ArrayList<String> modelsNames);

    @EntityGraph(value = "PrintModel-oth", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModelData> findAll(Specification<PrintModelData> modelSpecification, Pageable pageable);

    void deleteAllByModelNameIn(Collection<String> deletModelSet);

    @EntityGraph(value = "PrintModel-oth", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModelData> findAllByModelTagsObjData_TagContaining(String nameTag, Pageable pageable); // TODO bad

    // old
    @EntityGraph(value = "PrintModel-oth", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModelData> findAllBymodelNameLikeIgnoreCase(String name, Pageable page);

    @Query("Select modelName from PrintModelData")
    List<String> getAllNameModel();

    PrintModelData getByModelName(String nameModel);




}
