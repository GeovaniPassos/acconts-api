package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.services.ExpensesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {

    private final ExpensesService service;

    public ExpensesController(ExpensesService service) {this.service = service;}

    @PostMapping
    public ResponseEntity<ExpensesDto> create(@RequestBody ExpensesDto dto) {
        ExpensesDto result = service.createExpenses(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
