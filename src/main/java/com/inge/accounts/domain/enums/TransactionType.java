package com.inge.accounts.domain.enums;

import com.inge.accounts.exceptions.BusinessException;

public enum TransactionType {
    EXPENSES,
    RECEIPT;

    public static TransactionType fromString(String value) {
        try {
            return TransactionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo '" + value + "', é inválido");
        }
    }
}