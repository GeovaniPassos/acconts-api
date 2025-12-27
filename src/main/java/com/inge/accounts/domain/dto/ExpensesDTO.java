package com.inge.accounts.domain.dto;

import java.util.Date;

public record ExpensesDTO(Long id,
                          String name,
                          String description,
                          double value,
                          String categoryName,
                          boolean payment,
                          Date date
) {
}
