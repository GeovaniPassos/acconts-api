package com.inge.accounts.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpensesDto(Long id,
                          String name,
                          String description,
                          String categoryName,
                          int installment,
                          int totalInstallments,
                          boolean payment,
                          BigDecimal value,
                          LocalDate paymentDate,
                          LocalDate date
) {
}
