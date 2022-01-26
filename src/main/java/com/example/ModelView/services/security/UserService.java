package com.example.ModelView.services.security;

import com.example.ModelView.dto.security.UserRegistrationDto;
import com.example.ModelView.entities.security.Watcher;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Watcher save(UserRegistrationDto registrationDto);
}
