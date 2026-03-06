package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.CategoryDto;
import com.inge.accounts.domain.dto.CategoryPatchDto;
import com.inge.accounts.response.ApiResponse;
import com.inge.accounts.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody CategoryDto dto) {
        CategoryDto result = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Categoria criada com sucesso"));
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<List<CategoryDto>>> findAll() {

        List<CategoryDto> list = service.findAll();

        if (list.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.empty("Nunhuma categoria encontrada com o filtro informado"));
        }

        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> findById(@PathVariable Long id) {

        return service.findById(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success("Categoria encontrada", dto)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Categoria não encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Categoria removida com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody CategoryDto dto) {
        CategoryDto updated = service.update(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Categoria atualizada com sucesso"));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<ApiResponse<Void>> patch(@PathVariable Long id, @RequestBody CategoryPatchDto dto) {
        CategoryDto patch = service.patch(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Categoria atualizada com sucesso"));
    }
}
