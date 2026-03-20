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

        service.addInstallmentsByUser(dto, username);
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
            response = service.findAllByUser(username);

            return ResponseEntity.ok(ApiResponse.success("Consulta realizada", response));
        }

        response = service.findExpensesByUser(startDate, endDate, name, username);

        return ResponseEntity.ok(ApiResponse.success("Consulta realizada", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpensesDto>> findById(@PathVariable Long id, Authentication authentication) {

        String username = authentication.getName();
        ExpensesDto expense = service.findByIdAndUser(id, username);

        return ResponseEntity.ok(
                ApiResponse.success("Resultado da busca: ", expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id,
                                                    Authentication authentication) {
        String username = authentication.getName();

        service.delete(id, username);
        return ResponseEntity.ok(ApiResponse.success("Despesa removida com sucesso"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> patch(@PathVariable @NotNull Long id,
                                                   @RequestBody ExpensesPatchDto dto,
                                                   Authentication authentication) {
        String username = authentication.getName();

        service.patch(id, dto, username);
        return ResponseEntity.ok(ApiResponse.success("Despesa atualizada com sucesso"));
    }

    @PatchMapping("/{id}/toggle-payment")
    public ResponseEntity<ApiResponse<Void>> togglePayment(@PathVariable @NotNull Long id, Authentication authentication) {

        String username = authentication.getName();

        service.togglePaymentByUser(id, username);
        return ResponseEntity.ok(ApiResponse.success("Pagamento atualizado"));
    }


}
