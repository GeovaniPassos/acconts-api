package com.inge.accounts.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReceiptSearchResponseDto {

    private List<ReceiptDto> receipt;
    private BigDecimal total;
    private BigDecimal totalRemaining;

    public ReceiptSearchResponseDto(List<ReceiptDto> receipt, BigDecimal total, BigDecimal totalRemaining) {
        this.receipt = receipt;
        this.total = total;
        this.totalRemaining = totalRemaining;
    }

    public List<ReceiptDto> getReceipt() {
        return receipt;
    }

    public BigDecimal getTotal() { return total; }

    public BigDecimal getTotalRemaining() {
        return totalRemaining;
    }
}
