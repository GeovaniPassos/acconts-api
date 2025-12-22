package com.inge.accounts.controllers;

import com.inge.accounts.dtos.CategoryDto;
import com.inge.accounts.entity.Category;
import com.inge.accounts.enums.TransactionType;
import com.inge.accounts.services.CategoryService;
import org.hibernate.query.sqm.tree.expression.NumericTypeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto, TransactionType type) {
        CategoryDto result = service.findOrCreate(dto.name(), type);
        return ResponseEntity.ok(result);
    }
}
