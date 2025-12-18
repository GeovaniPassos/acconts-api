package com.inge.accounts.dtos;

import com.inge.accounts.entity.Category;

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
