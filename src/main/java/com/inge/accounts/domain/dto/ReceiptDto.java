package com.inge.accounts.domain.dto;

import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.validations.OnCreate;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

public record ReceiptDto(Long id,
                         @NotBlank(groups = OnCreate.class,
                                 message="O nome não pode ser nulo.")
                         String name,
                         String description,
                         @NotBlank(groups = OnCreate.class,
                                message="A categoria não pode ser nula.")
                         String categoryName,
                         BigDecimal value,
                         Date date
) {}