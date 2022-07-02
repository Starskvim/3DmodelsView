package com.example.ModelView.persistance.repositories.web;

import com.example.ModelView.model.entities.web.PrintModelTagWebData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepositoryTagsWeb extends JpaRepository<PrintModelTagWebData, Long> {

    @Query("Select nameTag from PrintModelTagWebData")
    List<String> getAllNameTags();

    @EntityGraph(value = "TagWithModels", type = EntityGraph.EntityGraphType.LOAD)
    List<PrintModelTagWebData> findAll();


}
