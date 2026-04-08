package com.inge.accounts.repository;

import com.inge.accounts.domain.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByIdAndUserId(Long id, Long userId);
}
