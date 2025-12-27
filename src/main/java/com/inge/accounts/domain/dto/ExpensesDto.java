package com.inge.accounts.domain.dto;

import java.util.Date;

public record ExpensesDto(Long id,
                          String name,
                          String description,
                          String categoryName,
                          boolean payment,
                          double value,
                          Date date
) {
}
