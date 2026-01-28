package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.dto.ExpensesPatchDto;
import com.inge.accounts.services.ExpensesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {

    private final ExpensesService service;

    public ExpensesController(ExpensesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ExpensesDto> create(@RequestBody ExpensesDto dto) {
        ExpensesDto result = service.createExpenses(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ExpensesDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpensesDto> patch(@PathVariable Long id, @RequestBody ExpensesPatchDto dto) {
        return ResponseEntity.ok(service.patch(id, dto));
    }

    @GetMapping("/by-period")
    public ResponseEntity<List<ExpensesDto>> findByPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(service.findByPeriod(startDate, endDate));
    }

    @GetMapping("/by-month")
    public ResponseEntity<List<ExpensesDto>> findByMonth(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(service.findByMonth(YearMonth.of(year, month)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpensesDto>> findByNameContainsIgnoreCase(@RequestParam String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PatchMapping("/{id}/toggle-payment")
    public ResponseEntity<ExpensesDto> togglePayment(@PathVariable Long id) {
        return ResponseEntity.ok(service.togglePayment(id));
    }
}
