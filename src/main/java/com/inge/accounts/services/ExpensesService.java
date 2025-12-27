package com.inge.accounts.services;

import com.inge.accounts.domain.dto.ExpensesDTO;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;
    @Autowired
    private CategoryService categoryService;

    public ExpensesService(ExpensesRepository expensesRepository,
                           CategoryService categoryService) {
        this.expensesRepository = expensesRepository;
        this.categoryService = categoryService;
    }

    public ExpensesDTO createExpenses(ExpensesDTO dto) {

        Category category = categoryService.findByNameAndType(dto.categoryName(), TransactionType.EXPENSES);

        TransactionType type = TransactionType.fromString(categoryDto.type());


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
