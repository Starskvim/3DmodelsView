package com.example.ModelView.repositories.jpa;

import com.example.ModelView.entities.security.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface WatchersRepository extends JpaRepository<Watcher, Long> {
    Watcher findByEmail(String email);
}
