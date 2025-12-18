package com.inge.accounts.services;

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
    public Category findOrCreate(String name, TransactionType type) {
        return repository.findByNameAndType(name, type).orElseGet(() -> {
           Category newCategory = new Category(name, type);
           return repository.save(newCategory);
        });
    }
}
