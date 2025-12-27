package com.inge.accounts.services;

import com.inge.accounts.domain.dto.ExpensesDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.ExpensesMapper;
import com.inge.accounts.repository.ExpensesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
