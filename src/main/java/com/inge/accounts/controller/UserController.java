package com.inge.accounts.controller;

import com.inge.accounts.domain.entity.User;
import com.inge.accounts.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private final UserDetailsService service;

    public UserController(UserDetailsService service) {
        this.service = service;
    }

    public User findAll(){
        return null;
    }
}
