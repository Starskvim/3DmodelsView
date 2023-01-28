package com.example.ModelView.persistance.repositories;

import com.example.ModelView.model.entities.security.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WatchersRepository extends JpaRepository<Watcher, Long> {

    Watcher findByEmail(String email);
}
