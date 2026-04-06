package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.ReceiptDto;
import com.inge.accounts.domain.validations.OnCreate;
import com.inge.accounts.response.ApiResponse;
import com.inge.accounts.services.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/receipt")
public class ReceiptController {

    private final ReceiptService service;

    public ReceiptController(ReceiptService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Validated(OnCreate.class)
                                                    @RequestBody ReceiptDto dto,
                                                    Authentication authentication) {
        String username = authentication.getName();

        service.createReceipt(username, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Receita criada com sucesso"));
    }
}
