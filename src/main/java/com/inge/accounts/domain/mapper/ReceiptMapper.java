package com.inge.accounts.domain.mapper;

import com.inge.accounts.domain.dto.ExpensesAddInstallmentsDto;
import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.dto.ReceiptDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.entity.Receipt;
import com.inge.accounts.domain.entity.User;

import java.math.BigDecimal;

public class ReceiptMapper {

    private ReceiptMapper() {}

    public static Receipt toEntity(ReceiptDto dto, Category category, User user) {
        if (dto == null) return null;

        Receipt receipt = new Receipt();
        receipt.setName(dto.name());
        receipt.setDescription(dto.description());
        receipt.setValue(dto.value());
        receipt.setCategory(category);
        receipt.setDate(dto.date());
        receipt.setUser(user);

        return receipt;
    }

    public static ReceiptDto toDto(Receipt receipt) {
        if (receipt == null) return null;

        return new ReceiptDto(
                receipt.getId(),
                receipt.getName(),
                receipt.getDescription(),
                receipt.getCategory().getName(),
                receipt.getValue(),
                receipt.getDate()
        );
    }
}

