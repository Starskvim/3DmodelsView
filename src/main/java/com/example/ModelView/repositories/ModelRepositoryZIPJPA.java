package com.example.ModelView.repositories;

import com.example.ModelView.entities.ModelZIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepositoryZIPJPA extends JpaRepository<ModelZIP, Long> {

}
