package com.inge.accounts.domain.mapper;

import com.inge.accounts.domain.dto.ExpensesAddInstallmentsDto;
import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;

import java.math.BigDecimal;

public class ExpensesMapper {
    
    private ExpensesMapper() {}
    
    public static Expenses toEntity(ExpensesDto dto, Category category) {
        if (dto == null) return null;

        Expenses expenses = new Expenses();
        expenses.setName(dto.name());
        expenses.setDescription(dto.description());
        expenses.setCategory(category);
        expenses.setInstallment(dto.installment());
        expenses.setTotalInstallments(dto.totalInstallments());
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
                entity.getInstallment(),
                entity.getTotalInstallments(),
                entity.isPayment(),
                entity.getValue(),
                entity.getPaymentDate(),
                entity.getDate()
        );
    }

    public static Expenses copyExpensesAddInstallments(Expenses expenses, int newTotalInstallments) {
        Expenses newExpenses = new Expenses();

        newExpenses.setName(expenses.getName());
        newExpenses.setDescription(expenses.getDescription());
        newExpenses.setCategory(expenses.getCategory());
        newExpenses.setInstallment(expenses.getInstallment());
        newExpenses.setTotalInstallments(newTotalInstallments);
        newExpenses.setValue(BigDecimal.valueOf(0));
        newExpenses.setPayment(false);
        newExpenses.setPaymentDate(null);
        newExpenses.setDate(expenses.getDate());

        return  newExpenses;
    }

    public static ExpensesAddInstallmentsDto toAddInstallmentsDto(ExpensesDto expensesDto) {
        if (expensesDto == null) return null;

        return new ExpensesAddInstallmentsDto(
                expensesDto.name(),
                expensesDto.categoryName(),
                expensesDto.totalInstallments(),
                expensesDto.value()
        );

    }
}
