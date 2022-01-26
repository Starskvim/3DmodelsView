package com.example.ModelView.repositories.jpa;

import com.example.ModelView.entities.PrintModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


public interface ModelRepositoryJPA extends JpaRepository <PrintModel, Long>, JpaSpecificationExecutor<PrintModel> {

    Page<PrintModel> findAllBymodelNameLikeIgnoreCase(String name, Pageable page);

    @Query("Select modelName from PrintModel")
    List<String> getAllNameModel();

    PrintModel getByModelName(String nameModel);

    void deleteAllByModelNameIn(Collection<String> deletModelSet);



}
