package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepository  extends JpaRepository<Expenses, Long> {
    List<Expenses> findAllByUserId(Long id);
    Optional<Expenses> findByIdAndUserId(Long id, Long userId);
    boolean existsByNameAndUserId(String name, Long userId);

    @Query("""
            SELECT e
            FROM Expenses e
            WHERE e.user.id = :userId
            AND :startDate IS NULL OR e.date >= :startDate
            AND :endDate IS NULL OR e.date <= :endDate
            AND :name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY e.date, e.name
            """)
    List<Expenses> findExpensesByUserId(LocalDate startDate, LocalDate endDate, String name, Long userId);

    @Query(value = """
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM tb_expenses e
            WHERE e.user_id = :userId
            AND (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', CAST(:name AS text), '%')))
            """, nativeQuery = true)
    BigDecimal sumValueTotalExpensesByUserId(LocalDate startDate, LocalDate endDate, String name, Long userId);

    @Query(value = """
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM tb_expenses e
            WHERE e.user_id = :userId
            AND (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', CAST(:name AS text), '%')))
            AND e.payment = true
            """, nativeQuery = true)
    BigDecimal sumValueTotalPaidExpensesByUserId(LocalDate startDate, LocalDate endDate, String name, Long userId);

    @Query(value = """
            SELECT
                COALESCE(SUM(e.value), 0)
            FROM tb_expenses e
            WHERE e.user_id = :userId
            AND (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)
            AND (:name IS NULL OR LOWER(e.name)
                LIKE LOWER(CONCAT('%', CAST(:name AS text), '%')))
            AND e.payment = false
            """, nativeQuery = true)
    BigDecimal sumValueTotalUnpaidExpensesByUserId(LocalDate startDate, LocalDate endDate, String name, Long userId);
}