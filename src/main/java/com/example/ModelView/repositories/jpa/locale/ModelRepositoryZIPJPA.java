package com.example.ModelView.repositories.jpa.locale;

import com.example.ModelView.entities.locale.ModelZIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ModelRepositoryZIPJPA extends JpaRepository<ModelZIP, Long> {

    @Query("Select nameModelZIP from ModelZIP ")
    List<String> getAllnameModelZIP();

    @Query("Select modelZIPAdress from ModelZIP ")
    List<String> getAllmodelZIPAdress();

    ModelZIP getModelZIPByModelZIPAdress(String adress);


}
