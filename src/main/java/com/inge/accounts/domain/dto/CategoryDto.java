package com.inge.accounts.domain.dto;

import com.inge.accounts.domain.validations.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(Long id,
                          @NotBlank(groups= OnCreate.class, message="O nome é obrigatório no cadastro.")
                          String name,
                          @NotBlank(groups= OnCreate.class, message="O tipo é obrigatório no cadastro.")
                          String type
                          ) { }
