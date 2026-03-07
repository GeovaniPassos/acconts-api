package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.dto.CategoryPatchDto;
import com.inge.accounts.domain.validations.OnUpdate;
import com.inge.accounts.response.ApiResponse;
import com.inge.accounts.services.CategoryService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.inge.accounts.domain.validations.OnCreate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Validated(OnCreate.class) @RequestBody CategoryDto dto) {
        service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Categoria criada com sucesso"));
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<List<CategoryDto>>> findAll() {

        List<CategoryDto> list = service.findAll();

        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> findById(@PathVariable @NonNull Long id) {

        CategoryDto list = service.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca", list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Categoria removida com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@Validated(OnUpdate.class)  @PathVariable @NonNull Long id, @RequestBody CategoryDto dto) {
        service.update(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Categoria atualizada com sucesso"));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<ApiResponse<Void>> patch(@PathVariable @NonNull Long id, @RequestBody CategoryPatchDto dto) {
        service.patch(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Categoria atualizada com sucesso"));
    }
}
