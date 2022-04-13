package com.example.ModelView.repositories.jpa.web;

import com.example.ModelView.entities.web.PrintModelTagWeb;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepositoryTagsWeb extends JpaRepository<PrintModelTagWeb, Long> {

    @Query("Select nameTag from PrintModelTagWeb")
    List<String> getAllNameTags();

    @EntityGraph(value = "TagWithModels", type = EntityGraph.EntityGraphType.LOAD)
    List<PrintModelTagWeb> findAll();


}
