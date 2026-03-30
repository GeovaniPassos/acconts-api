package com.inge.accounts.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
