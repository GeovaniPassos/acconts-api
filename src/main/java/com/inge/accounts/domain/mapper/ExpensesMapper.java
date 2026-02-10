package com.inge.accounts.domain.mapper;

import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;

public class ExpensesMapper {
    
    private ExpensesMapper() {}
    
    public static Expenses toEntity(ExpensesDto dto, Category category) {
        if (dto == null) return null;

        Expenses expenses = new Expenses();
        expenses.setName(dto.name());
        expenses.setDescription(dto.description());
        expenses.setCategory(category);
        expenses.setPayment(dto.payment());
        expenses.setValue(dto.value());
        expenses.setPaymentDate(dto.paymentDate());
        expenses.setDate(dto.date());

        return expenses;
    }

    public static ExpensesDto toDto(Expenses entity) {
        if (entity == null) return null;

        return new ExpensesDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory().getName(),
                entity.isPayment(),
                entity.getValue(),
                entity.getPaymentDate(),
                entity.getDate()
        );
    }
}
