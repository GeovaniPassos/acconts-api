package com.inge.accounts.domain.dto;

import com.inge.accounts.domain.entity.Category;

import java.util.Date;

public record ReceiptDTO(Long id,
                         String name,
                         String description,
                         double value,
                         Category category,
                         Date date) {}