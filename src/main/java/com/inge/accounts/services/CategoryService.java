package com.inge.accounts.services;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.CategoryMapper;
import com.inge.accounts.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;
    private CategoryDto dto;

    @Transactional
    public CategoryDto create(CategoryDto dto) {

        TransactionType type = TransactionType.fromString(dto.type());

        if (repository.existsByNameAndType(dto.name(), type)) {
            throw new RuntimeException("Categoria já existe com o ID: " + repository.getReferenceById(dto.id()));
        }

        Category category =  CategoryMapper.toEntity(dto);
        category = repository.save(category);

        return CategoryMapper.toDto(category);

    }

    public List<CategoryDto> findAll() {
        return repository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    public Category findByNameAndType(String name, TransactionType type) {

        return repository.findByNameAndType(name, type);

    }
}
