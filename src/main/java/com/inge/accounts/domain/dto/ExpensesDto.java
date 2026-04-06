package com.inge.accounts.domain.dto;

import com.inge.accounts.domain.validations.OnCreate;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpensesDto(Long id,
                          @NotBlank(groups= OnCreate.class,
                                  message="O nome não pode ser nulo.")
                          String name,
                          String description,
                          @NotBlank(groups= OnCreate.class,
                                  message="A categoria não pode ser nula.")
                          String categoryName,
                          int installment,
                          int totalInstallments,
                          Boolean payment,
                          BigDecimal value,
                          LocalDate paymentDate,
                          LocalDate date
) {}