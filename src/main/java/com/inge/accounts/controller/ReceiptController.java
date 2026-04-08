package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.ReceiptDto;
import com.inge.accounts.domain.dto.ReceiptPatchDto;
import com.inge.accounts.domain.dto.ReceiptSearchResponseDto;
import com.inge.accounts.domain.validations.OnCreate;
import com.inge.accounts.response.ApiResponse;
import com.inge.accounts.services.ReceiptService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/receipt")
public class ReceiptController {

    @Autowired
    private final ReceiptService service;

    public ReceiptController(ReceiptService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Validated(OnCreate.class)
                                                    @RequestBody ReceiptDto dto,
                                                    Authentication authentication) {
        String username = authentication.getName();

        service.createReceiptByUser(username, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Receita criada com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReceiptDto>> findById(@PathVariable Long id, Authentication authentication) {

        String username = authentication.getName();

        ReceiptDto receipt = service.findByIdAndUser(id, username);

        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca: ", receipt));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ReceiptSearchResponseDto>> findReceipt(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String name,
            Authentication authentication
            ){

        String username = authentication.getName();

        ReceiptSearchResponseDto response;

        response = service.findReceiptByUser(startDate, endDate, name, username);

        return ResponseEntity.ok(ApiResponse.success("Consulta realizada", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication authentication) {

        String user = authentication.getName();
        service.deleteByUser(id, user);

        return ResponseEntity.ok(ApiResponse.success("Receita removida com sucesso."));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> patch(@PathVariable @NotNull Long id,
                                                   @RequestBody ReceiptPatchDto dto,
                                                   Authentication authentication) {
        String username = authentication.getName();

        service.patchByUser(id, dto, username);
        return ResponseEntity.ok(ApiResponse.success("Receita atualizada com sucesso."));
    }
}
