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
            WHERE (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', :name, '%')))
            ORDER BY e.name
            """)
    List<Expenses> findExpenses(LocalDate startDate, LocalDate endDate, String name);

    @Query(value = """
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM tb_expenses e
            WHERE (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', :name, '%')))
            """, nativeQuery = true)
    BigDecimal sumValueTotalExpenses(LocalDate startDate, LocalDate endDate, String name);

    @Query(value = """
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM tb_expenses e
            WHERE (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', :name, '%')))
            AND e.payment = true
            """, nativeQuery = true)
    BigDecimal sumValueTotalPaidExpenses(LocalDate startDate, LocalDate endDate, String name);

    @Query(value = """
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM tb_expenses e
            WHERE (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', :name, '%')))
            AND e.payment = false
            """, nativeQuery = true)
    BigDecimal sumValueTotalUnpaidExpenses(LocalDate startDate, LocalDate endDate, String name);
}