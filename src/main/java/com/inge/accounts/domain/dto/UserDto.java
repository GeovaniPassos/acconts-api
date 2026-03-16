package com.inge.accounts.domain.dto;

public record UserDto(Long id,
                      String username,
                      String password,
                      String roles
                      ) { }