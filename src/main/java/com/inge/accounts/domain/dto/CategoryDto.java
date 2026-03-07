package com.inge.accounts.domain.dto;

import com.inge.accounts.domain.validations.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(Long id,
                          @NotNull(groups= OnCreate.class, message="O nome é obrigatório no cadastro.")
                          @NotBlank(groups= OnCreate.class, message="O nome não pode ser em branco.")
                          String name,
                          @NotNull(groups= OnCreate.class, message="O tipo é obrigatório no cadastro.")
                          @NotBlank(groups= OnCreate.class, message="O tipo não pode ser em branco.")
                          String type
                          ) { }
