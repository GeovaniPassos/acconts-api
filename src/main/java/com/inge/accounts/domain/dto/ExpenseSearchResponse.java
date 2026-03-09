package com.inge.accounts.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseSearchResponse {
    private List<ExpensesDto> expenses;
    private BigDecimal total;
    private BigDecimal totalPaid;
    private BigDecimal totalUnpaid;
}
