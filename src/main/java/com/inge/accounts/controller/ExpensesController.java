package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.ExpensesAddInstallmentsDto;
import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.dto.ExpensesPatchDto;
import com.inge.accounts.domain.validations.OnCreate;
import com.inge.accounts.response.ApiResponse;
import com.inge.accounts.services.ExpensesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<ApiResponse<Void>> create(@Validated(OnCreate.class) @RequestBody ExpensesDto dto) {
        service.createExpenses(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Despesa criada com sucesso"));
    }

    @PostMapping("/addInstallments")
    public ResponseEntity<ApiResponse<Void>> addInstallments( @RequestBody ExpensesAddInstallmentsDto dto) {
        service.addInstallments(dto);
        return ResponseEntity.ok(ApiResponse
                .success("Parcela(s) da(s) despesa atualizada com sucesso"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findAll() {
        List<ExpensesDto> list = service.findAll();

        if(list.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.empty("Nenhuma despesa encontrada com o filtro informado"));
        }

        return ResponseEntity.ok(ApiResponse.success("Resultado da busca", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpensesDto>> findById(@PathVariable Long id) {

        ExpensesDto expense = service.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca: ", expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Despesa removida com sucesso"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> patch(@PathVariable Long id, @RequestBody ExpensesPatchDto dto) {
        service.patch(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Despesa atualizada com sucesso"));
    }

    @GetMapping("/by-period")
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findByPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<ExpensesDto> list = service.findByPeriod(startDate, endDate);

        if (list.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.empty("Nenhuma despesa encontrada no periodo"));
        }

        return ResponseEntity.ok(ApiResponse.success("Lista de resultado", list));
    }

    @GetMapping("/by-month")
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findByMonth(@RequestParam int year, @RequestParam int month) {
        List<ExpensesDto> list = service.findByMonth(YearMonth.of(year, month));

        if (list.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.empty("Nenhuma despesa encontrada no periodo"));
        }

        return ResponseEntity.ok(ApiResponse.success("Resultado da busca", list));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findByNameContainsIgnoreCase(@RequestParam String name) {
        List<ExpensesDto> list = service.findByName(name);

        if (list.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.empty("Nenhuma despesa encontrada por nome"));
        }

        return ResponseEntity.ok(ApiResponse.success("Resultado da busca", list));
    }

    @PatchMapping("/{id}/toggle-payment")
    public ResponseEntity<ApiResponse<Void>> togglePayment(@PathVariable Long id) {
        service.togglePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento atualizado"));
    }
}
