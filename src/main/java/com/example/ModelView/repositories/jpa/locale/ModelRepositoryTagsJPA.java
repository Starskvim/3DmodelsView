package com.example.ModelView.repositories.jpa.locale;


import com.example.ModelView.entities.locale.ModelTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModelRepositoryTagsJPA extends JpaRepository<ModelTag, Long> {

    @Query("Select tag from ModelTag")
    List<String> getAllNameTags();

}
