package com.inge.accounts.services;

import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.dto.ExpensesPatchDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.ExpensesMapper;
import com.inge.accounts.repository.ExpensesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final CategoryService categoryService;

    public ExpensesService(ExpensesRepository expensesRepository,
                           CategoryService categoryService) {
        this.expensesRepository = expensesRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public ExpensesDto createExpenses(ExpensesDto dto) {

        Category category = categoryService.findByNameAndType(
                dto.categoryName(),
                TransactionType.EXPENSES);

        Expenses expenses = ExpensesMapper.toEntity(dto, category);

        expenses = expensesRepository.save(expenses);

        return ExpensesMapper.toDto(expenses);

    }

    @Transactional
    public List<ExpensesDto> findAll() {
        return expensesRepository.findAll()
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();
    }

    public ExpensesDto findById(Long id) {
        return expensesRepository.findById(id)
                .map(ExpensesMapper::toDto)
                .orElseThrow(() ->
                        new RuntimeException("Despesa não encontrada!"));
    }

    public void delete(Long id) {
        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada!"));
        expensesRepository.delete(expenses);
    }

    @Transactional
    public ExpensesDto patch(Long id, ExpensesPatchDto dto) {

        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Despesa não encontrada com id: " + id
                ));

        if (dto.name() != null) {
            expenses.setName(dto.name());
        }

        if (dto.description() != null) {
            expenses.setDescription(dto.description());
        }

        if (dto.categoryName() != null) {
            Category category = categoryService.findByNameAndType(
                    dto.categoryName(), TransactionType.EXPENSES);
            expenses.setCategory(category);
        }

        if (dto.payment() != null) {
            expenses.setPayment(dto.payment());
        }

        if (dto.value() != null) {
            expenses.setValue(dto.value());
        }

        if (dto.date() != null) {
            expenses.setDate(dto.date());
        }

        return ExpensesMapper.toDto(expenses);
    }

    @Transactional
    public List<ExpensesDto> findByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Data inicial não pode ser maior que a final.");
        }

        return expensesRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();
    }

    @Transactional
    public List<ExpensesDto> findByMonth(YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return expensesRepository.findByDateBetween(start, end)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();
    }
}
