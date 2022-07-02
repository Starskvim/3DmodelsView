package com.example.ModelView.services.security;

import com.example.ModelView.model.rest.security.UserRegistrationDto;
import com.example.ModelView.model.entities.security.Watcher;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Watcher save(UserRegistrationDto registrationDto);
}
