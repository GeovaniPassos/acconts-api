package com.inge.accounts.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

public record ExpensesDto(Long id,
                          String name,
                          String description,
                          String categoryName,
                          boolean payment,
                          BigDecimal value,
                          Date date
) {
}
