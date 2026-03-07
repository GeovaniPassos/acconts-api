package com.inge.accounts.services;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.dto.CategoryPatchDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.CategoryMapper;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.CategoryRepository;
import com.inge.accounts.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    private CategoryDto dto;

    @Transactional
    public void create(CategoryDto dto) {
        if (StringUtils.isNullOrBlank(dto.name())) {
            throw new BusinessException("O nome está vazio");
        }

        if (StringUtils.isNullOrBlank(dto.type())) {
            throw new BusinessException("O tipo está vazio");
        }

        TransactionType type = TransactionType.fromString(dto.type());

        if (categoryRepository.existsByNameAndType(dto.name(), type)) {
            Category cat = categoryRepository.findByNameAndType(dto.name(), type);
            throw new BusinessException("Categoria já existe com o id: "
                    + cat.getId());
        }

        Category category = CategoryMapper.toEntity(dto);
        categoryRepository.save(category);

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


    public Optional<CategoryDto> findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toDto);
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
