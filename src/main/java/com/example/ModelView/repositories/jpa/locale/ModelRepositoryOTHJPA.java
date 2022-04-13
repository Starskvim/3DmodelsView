package com.example.ModelView.repositories.jpa.locale;

import com.example.ModelView.entities.locale.ModelOTH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ModelRepositoryOTHJPA extends JpaRepository <ModelOTH, Long>  {

    @Query("Select nameModelOTH from ModelOTH ")
    List<String> getAllnameModelOTH();

    @Query("Select modelOTHAdress from ModelOTH ")
    List<String> getAllmodelOTHAdress();

    ModelOTH getModelOTHByModelOTHAdress(String adress);

}
