package com.inge.accounts.services;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.dto.CategoryPatchDto;
import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.dto.ExpensesPatchDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.CategoryMapper;
import com.inge.accounts.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    private CategoryDto dto;

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        TransactionType type = TransactionType.fromString(dto.type());

        if (categoryRepository.existsByNameAndType(dto.name(), type)) {
            throw new RuntimeException("Categoria já existe com o ID: " + categoryRepository.getReferenceById(dto.id()));
        }

        Category category =  CategoryMapper.toEntity(dto);
        category = categoryRepository.save(category);

        return CategoryMapper.toDto(category);

    }

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    public Category findByNameAndType(String name, TransactionType type) {
        return categoryRepository.findByNameAndType(name, type);
    }


    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toDto)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada!"));
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada!"));

        categoryRepository.delete(category);

    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Despesa não encontrada!"));

        CategoryMapper.updateEntity(category, dto);

        return CategoryMapper.toDto(category);
    }

    @Transactional
    public CategoryDto patch(Long id, CategoryPatchDto dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de despesa não encontrada com id: " + id
                ));

        if (dto.name() != null) {
            category.setName(dto.name());
        }

        if (dto.type() != null) {
            category.setType(TransactionType.fromString(dto.type()));
        }

        return CategoryMapper.toDto(category);
    }

}
