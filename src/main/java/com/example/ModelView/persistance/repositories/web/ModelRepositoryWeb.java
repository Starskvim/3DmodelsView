package com.example.ModelView.persistance.repositories.web;

import com.example.ModelView.model.entities.web.PrintModelWebData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepositoryWeb extends JpaRepository<PrintModelWebData, Long>, JpaSpecificationExecutor<PrintModelWebData> {

    // actual
    @EntityGraph(value = "ForPrintModelPreview", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModelWebData> findAllBymodelNameLikeIgnoreCase(String name, Pageable page);

    @EntityGraph(value = "ForPrintModelPage", type = EntityGraph.EntityGraphType.LOAD)
    Optional<PrintModelWebData> findById(Long id);

    Page<PrintModelWebData> findAllByModelTags_NameTagContaining(String nameTag, Pageable pageable); // TODO bad

    @Query("Select modelName from PrintModelWebData")
    List<String> getAllNameModel();

    // old
    PrintModelWebData getByModelName(String nameModel);

    void deleteAllByModelNameIn(Collection<String> deletModelSet);

}
