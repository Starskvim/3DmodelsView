package com.example.ModelView.persistance.repositories.locale;

import com.example.ModelView.model.entities.locale.PrintModelZipData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ModelRepositoryZip extends JpaRepository<PrintModelZipData, Long> {

    @Query("Select modelName from PrintModelZipData")
    List<String> getAllnameModelZIP();

    @Query("Select zipAddress from PrintModelZipData")
    List<String> getAllmodelZIPAdress();

    PrintModelZipData getModelZipDataByZipAddress(String adress);


}
