package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByUserId(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    Category findByNameAndTypeAndUserId(String name, TransactionType type, Long userId);
    boolean existsByNameAndTypeAndUserId(String name, TransactionType type, Long userId);

}


