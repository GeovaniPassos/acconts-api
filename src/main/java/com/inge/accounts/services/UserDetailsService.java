package com.inge.accounts.services;

import com.inge.accounts.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository repository;

    public UserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        com.inge.accounts.domain.entity.User user = repository.findByUsername(username)
                .orElseThrow();

        return org.springframework.security.core.userdetails.User
                .withUserDetails(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }


}
