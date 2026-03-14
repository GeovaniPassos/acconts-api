package com.inge.accounts.domain.dto;

import com.inge.accounts.domain.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpensesPatchDto(
    String name,
    String description,
    String categoryName,
    Integer installment,
    Integer totalInstallments,
    Boolean payment,
    BigDecimal value,
    LocalDate paymentDate,
    LocalDate date
) {}
