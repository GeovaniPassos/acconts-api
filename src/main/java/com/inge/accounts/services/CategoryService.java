package com.inge.accounts.services;

import com.inge.accounts.dtos.CategoryDto;
import com.inge.accounts.entity.Category;
import com.inge.accounts.enums.TransactionType;
import com.inge.accounts.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional
    public CategoryDto findOrCreate(String name, String type) {
        //TODO: Terminar de ajustar a conversão de categoru para categoryDto

        return repository.findByNameAndType(name, type).orElseGet(() -> {
           Category newCategory = new Category(name, TransactionType.fromString(type));
           return repository.save(newCategory);
        });
    }

    public Category create(String name) {
        return null;
    }
}
