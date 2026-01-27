package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpensesRepository  extends JpaRepository<Expenses, Long> {
    List<Expenses> findByDateBetween(LocalDate startDate, LocalDate endDate);
}


