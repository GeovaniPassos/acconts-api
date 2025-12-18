package com.inge.accounts.repository;

import com.inge.accounts.entity.Category;
import com.inge.accounts.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndType(String name, TransactionType type);
}
