package com.inge.accounts.auth.security;

import com.inge.accounts.domain.entity.User;
import com.inge.accounts.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = repository.findByUsername(username)
                .orElseThrow();

        return new CustomUserDetails(user);
    }
}
