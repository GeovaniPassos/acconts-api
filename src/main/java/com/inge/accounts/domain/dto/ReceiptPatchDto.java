package com.inge.accounts.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceiptPatchDto(
        String name,
        String description,
        String categoryName,
        BigDecimal value,
        LocalDate date) {}
