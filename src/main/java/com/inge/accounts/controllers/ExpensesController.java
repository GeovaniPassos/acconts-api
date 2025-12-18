package com.inge.accounts.controllers;

import com.inge.accounts.dtos.ExpensesDTO;
import com.inge.accounts.services.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService service;

    @PostMapping
    public ResponseEntity<ExpensesDTO> create(@RequestBody ExpensesDTO dto) {
        ExpensesDTO result;
        result = service.createExpenses(dto);
        return ResponseEntity.ok(result);
    }
}
