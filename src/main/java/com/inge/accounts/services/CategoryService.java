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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    private CategoryDto dto;

    @Transactional
    public CategoryDto create(CategoryDto dto) {

        TransactionType type = TransactionType.fromString(dto.type());

        if (categoryRepository.existsByNameAndType(dto.name(), type)) {
            Category cat = categoryRepository.findByNameAndType(dto.name(), type);
            throw new BusinessException("Categoria já existe com o id: "
                    + cat.getId());
        }

        Category category = CategoryMapper.toEntity(dto);
        return CategoryMapper.toDto(categoryRepository.save(category));

    }

    @Transactional
    public CategoryDto findOrCreate(String name, TransactionType type) {

        if (name == null && type == null) {
            throw new BusinessException("Precisa informar o nome e o tipo da categoria.");
        }
        Optional<CategoryDto> categories = Optional.ofNullable(
                CategoryMapper.toDto(
                categoryRepository.findByNameAndType(name, type)));

        return categories.orElseGet(() -> {
            Category category = new Category();
            category.setName(name);
            category.setType(type);
            return CategoryMapper.toDto(categoryRepository.save(category));
        });
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {

        List<CategoryDto> list = categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new BusinessException("Nenhuma categoria encontrada.");
        }

        return list;
    }

    @Transactional(readOnly = true)
    public CategoryDto findByNameAndType(String name, TransactionType type) {

        if (StringUtils.isNullOrBlank(name)) {
            throw new BusinessException("O nome não pode ser nulo.");
        }

        if (StringUtils.isNullOrBlank(type.toString())) {
            throw new BusinessException("O tipo não pode ser nulo.");
        }

        return CategoryMapper.toDto(categoryRepository.findByNameAndType(name, type));
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {

        if (id == null) {
            throw new BusinessException("O id é nulo.");
        }

        return categoryRepository.findById(id)
                .map(CategoryMapper::toDto).orElseThrow(() ->
                        new BusinessException("Categoria não encontrada com id: " + id));
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Categoria não encontrada!"));

        categoryRepository.delete(category);

    }

    @Transactional
    public void update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada!"));

        CategoryMapper.updateEntity(category, dto);
    }

    @Transactional
    public void patch(Long id, CategoryPatchDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de despesa não encontrada com id: " + id));

        CategoryMapper.copyNonNullProperties(category, dto);
    }
}
