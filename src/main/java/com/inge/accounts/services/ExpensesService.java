package com.inge.accounts.services;

import com.inge.accounts.dtos.ExpensesDTO;
import com.inge.accounts.entity.Category;
import com.inge.accounts.entity.Expenses;
import com.inge.accounts.enums.TransactionType;
import com.inge.accounts.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;
    @Autowired
    private CategoryService categoryService;

    public ExpensesDTO createExpenses(ExpensesDTO dto) {

        Category category = categoryService.findOrCreate(dto.categoryName(), TransactionType.EXPENSES);

        Expenses entity = new Expenses();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setValue(dto.value());
        entity.setCategory(category);
        entity.setPayment(dto.payment());
        entity.setDate(dto.date());

        entity = expensesRepository.save(entity);

        return new ExpensesDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getValue(),
                entity.getCategory().getName(),
                entity.isPayment(),
                entity.getDate());

    }
}
