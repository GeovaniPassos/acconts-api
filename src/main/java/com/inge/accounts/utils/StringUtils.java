package com.inge.accounts.utils;

public class StringUtils {

    private StringUtils(){}

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
