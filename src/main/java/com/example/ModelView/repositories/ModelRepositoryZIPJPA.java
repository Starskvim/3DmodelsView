package com.example.ModelView.repositories;

import com.example.ModelView.entities.ModelZIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Repository
public interface ModelRepositoryZIPJPA extends JpaRepository<ModelZIP, Long> {

    @Query("Select nameModelZIP from ModelZIP ")
    List<String> getAllnameModelZIP();
}
