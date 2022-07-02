package com.example.ModelView.persistance.repositories.locale;

import com.example.ModelView.model.entities.locale.PrintModelOthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ModelRepositoryOth extends JpaRepository <PrintModelOthData, Long>  {

    @Query("Select nameModelOth from PrintModelOthData ")
    List<String> getAllnameModelOTH();

    @Query("Select othAddress from PrintModelOthData ")
    List<String> getAllmodelOTHAdress();

    PrintModelOthData getPrintModelOthByOthAddress(String adress);

}
