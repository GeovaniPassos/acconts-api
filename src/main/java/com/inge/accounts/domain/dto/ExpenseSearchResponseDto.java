package com.inge.accounts.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseSearchResponseDto {
    private List<ExpensesDto> expenses;
    private BigDecimal total;
    private BigDecimal totalPaid;
    private BigDecimal totalUnpaid;

    public ExpenseSearchResponseDto(List<ExpensesDto> expenses, BigDecimal total, BigDecimal totalPaid, BigDecimal totalUnpaid) {
        this.expenses = expenses;
        this.total = total;
        this.totalPaid = totalPaid;
        this.totalUnpaid = totalUnpaid;
    }
}
