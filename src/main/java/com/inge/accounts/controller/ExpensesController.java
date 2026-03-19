package com.inge.accounts.controller;

import com.inge.accounts.domain.dto.ExpenseSearchResponseDto;
import com.inge.accounts.domain.dto.ExpensesAddInstallmentsDto;
import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.dto.ExpensesPatchDto;
import com.inge.accounts.domain.validations.OnCreate;
import com.inge.accounts.response.ApiResponse;
import com.inge.accounts.services.ExpensesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ApiResponse<Void>> create(@Validated(OnCreate.class) @RequestBody ExpensesDto dto,
                                                    Authentication authentication) {
        String username = authentication.getName();

        service.createExpenses(username, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Despesa criada com sucesso"));
    }

    @PostMapping("/addInstallments")
    public ResponseEntity<ApiResponse<Void>> addInstallments(@RequestBody ExpensesAddInstallmentsDto dto,
                                                             Authentication authentication) {
        String username = authentication.getName();

        service.addInstallments(dto, username);
        return ResponseEntity.ok(ApiResponse
                .success("Parcela(s) da(s) despesa atualizada com sucesso"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ExpenseSearchResponseDto>> findExpenses(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String name,
            Authentication authentication) {

        String username = authentication.getName();

        ExpenseSearchResponseDto response;

        if (startDate == null && endDate == null && name == null) {
            response = service.findAll(username);

            return ResponseEntity.ok(ApiResponse.success("Consulta realizada", response));
        }

        response = service.findExpenses(startDate, endDate, name, username);

        return ResponseEntity.ok(ApiResponse.success("Consulta realizada", response));
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
    public ResponseEntity<ApiResponse<Void>> patch(@PathVariable @NotNull Long id, @RequestBody ExpensesPatchDto dto) {
        service.patch(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Despesa atualizada com sucesso"));
    }

    @GetMapping("/by-period")
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findByPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<ExpensesDto> list = service.findByPeriod(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Lista de resultado", list));
    }

    @GetMapping("/by-month")
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findByMonth(@RequestParam int year, @RequestParam int month) {
        List<ExpensesDto> list = service.findByMonth(YearMonth.of(year, month));
        return ResponseEntity.ok(ApiResponse.success("Resultado da busca", list));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ExpensesDto>>> findByNameContainsIgnoreCase(@RequestParam String name) {
        List<ExpensesDto> list = service.findByName(name);
        return ResponseEntity.ok(ApiResponse.success("Resultado da busca", list));
    }

    @PatchMapping("/{id}/toggle-payment")
    public ResponseEntity<ApiResponse<Void>> togglePayment(@PathVariable @NotNull Long id) {
        service.togglePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Pagamento atualizado"));
    }


}
