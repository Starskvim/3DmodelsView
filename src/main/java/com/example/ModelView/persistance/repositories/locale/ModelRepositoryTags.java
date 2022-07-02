package com.example.ModelView.persistance.repositories.locale;


import com.example.ModelView.model.entities.locale.PrintModelTagData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModelRepositoryTags extends JpaRepository<PrintModelTagData, Long> {

    @Query("Select tag from PrintModelTagData ")
    List<String> getAllNamesTags();

}
