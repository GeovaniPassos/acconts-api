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
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ApiResponse<Void>> createByUser(@Validated(OnCreate.class) @RequestBody CategoryDto dto,
                                                    Authentication authentication) {

        String username = authentication.getName();
        service.createByUser(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Categoria criada com sucesso"));
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<List<CategoryDto>>> findAllByUser(Authentication authentication) {

        String username = authentication.getName();
        List<CategoryDto> list = service.findAllByUser(username);
        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> findByIdAndUser(@PathVariable @NonNull Long id,
                                                             Authentication authentication) {
        String user = authentication.getName();
        CategoryDto list = service.findByIdAndUser(id, user);
        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca", list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteByUser(@PathVariable Long id, Authentication authentication) {

        String user = authentication.getName();
        service.deleteByUser(id, user);
        return ResponseEntity.ok(ApiResponse.success("Categoria removida com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateByUser(@Validated(OnUpdate.class)  @PathVariable @NonNull Long id,
                                                          @RequestBody CategoryDto dto,
                                                          Authentication authentication) {
        String user = authentication.getName();
        service.updateByUser(id, dto, user);
        return ResponseEntity.ok(ApiResponse.success("Categoria atualizada com sucesso"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> patchByUser(@PathVariable @NonNull Long id, @RequestBody CategoryPatchDto dto,
                                                          Authentication authentication) {
        String user = authentication.getName();
        service.patchByUser(id, dto, user);
        return ResponseEntity.ok(ApiResponse.success("Categoria atualizada com sucesso"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> findByNameContainsIgnoreCase(@RequestParam String name ) {
        List<CategoryDto> list = service.findBytus
        Name(name);

        return ResponseEntity.ok(ApiResponse.success("Resultado da busca", list));
    }
}
