package com.inge.accounts.dtos;

import com.inge.accounts.enums.TransactionType;

public record CategoryDto(Long id,
                          String name,
                          String type
                          ) { }
