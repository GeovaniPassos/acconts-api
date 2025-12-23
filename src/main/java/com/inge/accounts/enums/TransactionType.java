package com.inge.accounts.enums;

public enum TransactionType {
    EXPENSES,
    RECEIPT;

    public static TransactionType fromString(String value) {
        try {
            return TransactionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo '" + value + "', é inválido");
        }
    }
}