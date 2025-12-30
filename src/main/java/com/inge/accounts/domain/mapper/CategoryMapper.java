package com.inge.accounts.domain.mapper;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.enums.TransactionType;

public class CategoryMapper {

    private CategoryMapper() {}

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setName(dto.name());
        category.setType(TransactionType.fromString(dto.type()));

        return category;
    }

    public static CategoryDto toDto(Category entity) {
        if (entity == null) return null;

        return new CategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getType().name()
        );
    }

    public static void updateEntity(Category entity, CategoryDto dto) {
        entity.setName(dto.name());
        entity.setType(TransactionType.valueOf(dto.type()));
    }
}
