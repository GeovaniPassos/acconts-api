package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpensesRepository  extends JpaRepository<Expenses, Long> {
    List<Expenses> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expenses> findByNameContainsIgnoreCase(String name);
    List<Expenses> findByName(String name);

    @Query("""
            SELECT e
            FROM Expenses e
            WHERE (:startDate IS NULL OR e.date >= :startDate)
            AND (:endDate IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR e.name = :name)
            ORDER BY e.name
            """)
    List<Expenses> findExpenses(LocalDate startDate, LocalDate endDate, String name);

    @Query("""
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM Expenses e
            WHERE (:startDate IS NULL OR e.date >= :startDate)
            AND (:endDate IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR e.name = :name)
            """)
    BigDecimal sumValueTotalExpenses(LocalDate startDate, LocalDate endDate, String name);

    @Query("""
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM Expenses e
            WHERE (:startDate IS NULL OR e.date >= :startDate)
            AND (:endDate IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR e.name = :name)
            AND e.payment = true
            """)
    BigDecimal sumValueTotalPaidExpenses(LocalDate startDate, LocalDate endDate, String name);

    @Query("""
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM Expenses e
            WHERE (:startDate IS NULL OR e.date >= :startDate)
            AND (:endDate IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR e.name = :name)
            AND e.payment = false
            """)
    BigDecimal sumValueTotalUnpaidExpenses(LocalDate startDate, LocalDate endDate, String name);
}