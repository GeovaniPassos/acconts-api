package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepository  extends JpaRepository<Expenses, Long>, JpaSpecificationExecutor<Expenses> {
    List<Expenses> findAllByUserId(Long id);

    Optional<Expenses> findByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);
}