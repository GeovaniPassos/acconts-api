package com.inge.accounts.services;

import com.inge.accounts.domain.dto.UserDto;
import com.inge.accounts.domain.entity.User;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserDto dto) {

        if (repository.existsByUsername(dto.username())) {
            throw new BusinessException("Usuário já existe");
        }

        User user = new User();

        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole("USER");

        return repository.save(user);
    }
}
