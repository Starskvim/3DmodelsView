package com.example.ModelView.services.security;

import com.example.ModelView.dto.security.UserRegistrationDto;
import com.example.ModelView.entities.security.ERole;
import com.example.ModelView.entities.security.Role;
import com.example.ModelView.entities.security.Watcher;
import com.example.ModelView.repositories.jpa.WatchersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final WatchersRepository watchersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    public UserServiceImpl(WatchersRepository watchersRepository) {
//        this.watchersRepository = watchersRepository;
//    }

    @Override
    public Watcher save(UserRegistrationDto registrationDto) {
        Watcher watcher = new Watcher(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role(ERole.ROLE_USER)));

        return watchersRepository.save(watcher);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Watcher watcher = watchersRepository.findByEmail(username);
        if(watcher == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(watcher.getEmail(), watcher.getPassword(), mapRolesToAuthorities(watcher.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }

}
