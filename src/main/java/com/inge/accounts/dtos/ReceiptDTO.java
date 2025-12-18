package com.inge.accounts.dtos;

import com.inge.accounts.entity.Category;

import java.util.Date;

public record ReceiptDTO(Long id,
                         String name,
                         String description,
                         double value,
                         Category category,
                         Date date) {}