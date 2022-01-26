package com.example.ModelView.repositories.jpa;


import com.example.ModelView.entities.ModelOTH;
import com.example.ModelView.entities.ModelTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelRepositoryTagsJPA extends JpaRepository<ModelTag, Long> {

}
