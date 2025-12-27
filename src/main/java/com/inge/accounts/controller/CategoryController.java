package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        CategoryDto result = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /*
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        CategoryDto result = service.create(dto.name(), dto.type());

        return ResponseEntity.ok(result);
    }*/


}
