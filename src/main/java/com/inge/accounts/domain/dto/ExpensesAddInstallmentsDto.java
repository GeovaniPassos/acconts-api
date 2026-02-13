package com.inge.accounts.domain.dto;

import java.math.BigDecimal;

public record ExpensesAddInstallmentsDto(String name,
                                         String categoryName,
                                         int installments,
                                         BigDecimal value
) {
}
