package com.inge.accounts.services;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.dto.CategoryPatchDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.User;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.CategoryMapper;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.CategoryRepository;
import com.inge.accounts.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createByUser(CategoryDto dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        TransactionType type = TransactionType.fromString(dto.type());

        if (categoryRepository.existsByNameAndTypeAndUserId(dto.name(), type, user.getId())) {
            Category cat = categoryRepository.findByNameAndTypeAndUserId(dto.name(), type, user.getId());
            throw new BusinessException("Categoria já existe com o id: "
                    + cat.getId());
        }

        Category category = CategoryMapper.toEntity(dto);
        category.setUser(user);

        categoryRepository.save(category);
    }

    @Transactional
    public Category findOrCreateByUser(String name, TransactionType type, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        if (name == null || type == null) {
            throw new BusinessException("Precisa informar o nome e o tipo da categoria.");
        }

        Category category = categoryRepository.findByNameAndTypeAndUserId(name, type, user.getId());

        if (category == null) {
            category = new Category(name, type, user);
            return categoryRepository.save(category);
        }

        return category;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllByUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        List<CategoryDto> list = categoryRepository.findAllByUserId(user.getId())
                .stream()
                .map(CategoryMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new BusinessException("Nenhuma categoria encontrada.");
        }

        return list;
    }

    @Transactional(readOnly = true)
    public CategoryDto findByIdAndUser(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        if (id == null) {
            throw new BusinessException("O id é nulo.");
        }

        return categoryRepository.findByIdAndUserId(id, user.getId())
                .map(CategoryMapper::toDto).orElseThrow(() ->
                        new BusinessException("Categoria não encontrada com id: " + id));
    }

    @Transactional
    public void deleteByUser(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new BusinessException("Categoria não encontrada!"));

        categoryRepository.delete(category);
    }

    @Transactional
    public void updateByUser(Long id, CategoryDto dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada!"));

        CategoryMapper.updateEntity(category, dto);
    }

    @Transactional
    public void patchByUser(Long id, CategoryPatchDto dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de despesa não encontrada com id: " + id));

        CategoryMapper.copyNonNullProperties(category, dto);
    }
}
