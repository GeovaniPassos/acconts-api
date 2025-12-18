package com.inge.accounts.services;

import com.inge.accounts.dtos.ExpensesDTO;
import com.inge.accounts.entity.Expenses;
import com.inge.accounts.repository.ExpensesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpensesService {

    @Autowired
    ExpensesRepository repository;

    public ExpensesDTO createExpenses(ExpensesDTO dto) {

        Expenses entity = new Expenses();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setValue(dto.value());
        entity.setCategory(dto.category());
        entity.setPayment(dto.payment());
        entity.setDate(dto.date());

        entity = repository.save(entity);

        return new ExpensesDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getValue(),
                entity.getCategory(),
                entity.isPayment(),
                entity.getDate());

    }
}
