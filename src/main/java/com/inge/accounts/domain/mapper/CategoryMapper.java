package com.inge.accounts.domain.mapper;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.dto.CategoryPatchDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.exceptions.BusinessException;

public class CategoryMapper {

    private CategoryMapper() {}

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setId(dto.id());
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
        if (dto.name().isBlank() || dto.type().isBlank() ) {
            throw new BusinessException("Deve atualizar todos os campos!");
        }

        entity.setName(dto.name());
        entity.setType(TransactionType.valueOf(dto.type()));
    }

    public static void copyNonNullProperties(Category entity, CategoryPatchDto dto) {
        if (dto.name() != null && !dto.name().isBlank()) {
            entity.setName(dto.name());
        }
        if (dto.type() != null && !dto.type().isBlank()) {
            entity.setType(TransactionType.valueOf(dto.type()));
        }
    }
}
