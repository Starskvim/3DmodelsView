package com.example.ModelView.repositories.jpa.web;

import com.example.ModelView.entities.web.PrintModelWeb;
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
public interface ModelRepositoryWeb extends JpaRepository<PrintModelWeb, Long>, JpaSpecificationExecutor<PrintModelWeb> {

    // actual
    @EntityGraph(value = "ForPrintModelPreview", type = EntityGraph.EntityGraphType.LOAD)
    Page<PrintModelWeb> findAllBymodelNameLikeIgnoreCase(String name, Pageable page);

    @EntityGraph(value = "ForPrintModelPage", type = EntityGraph.EntityGraphType.LOAD)
    Optional<PrintModelWeb> findById(Long id);

    Page<PrintModelWeb> findAllByModelTags_NameTagContaining(String nameTag, Pageable pageable); // TODO bad

    @Query("Select modelName from PrintModelWeb")
    List<String> getAllNameModel();

    // old
    PrintModelWeb getByModelName(String nameModel);

    void deleteAllByModelNameIn(Collection<String> deletModelSet);

}
