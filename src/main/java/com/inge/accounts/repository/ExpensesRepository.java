package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpensesRepository  extends JpaRepository<Expenses, Long> {
    List<Expenses> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expenses> findByNameContainsIgnoreCase(String name);
    List<Expenses> findByName(String name);

    @Query("""
            SELECT
                COALESCE(SUM(e.amount), 0)
            FROM tb_expenses e 
            WHERE (:startDate IS NULL OR e.date >= :startDate)
            AND
            ORDER BY e.name
            """)
    List<Expenses> findExpenses(LocalDate startDate, LocalDate endDate, String name);

//    @Query("""
//            SELECT
//                COALESCE(SUM(e.amount), 0),
//                COALESCE(SUM(CASE WHEN e.payment = true), 0),
//                COALESCE(SUM(e.amount), 0),
//            FROM tb_expenses e
//            WHERE ()
//            AND
//            ORDER BY e.name
//            """)

}