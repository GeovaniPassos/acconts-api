package com.inge.accounts.services;

import com.inge.accounts.domain.dto.UserDto;
import com.inge.accounts.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

//        User user = repository.findByUsername(username)
//                .orElseThrow();
//
//        return Userdetails.User
//                .withUserDetails(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
    }

    public User createUser(UserDto dto) {
        User user = new User();


    }
}
