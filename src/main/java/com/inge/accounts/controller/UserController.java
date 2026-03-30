package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.UserDto;
import com.inge.accounts.domain.entity.User;
import com.inge.accounts.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User createUser(@RequestBody UserDto dto){
        return service.createUser(dto);
    }
}
