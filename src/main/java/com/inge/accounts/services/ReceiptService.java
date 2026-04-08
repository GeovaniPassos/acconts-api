package com.inge.accounts.services;

import com.inge.accounts.controller.ReceiptController;
import com.inge.accounts.domain.dto.ReceiptDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Receipt;
import com.inge.accounts.domain.entity.User;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.ReceiptMapper;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.ExpensesRepository;
import com.inge.accounts.repository.ReceiptRepository;
import com.inge.accounts.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public ReceiptService(ReceiptRepository receiptRepository,
                           CategoryService categoryService,
                           UserRepository userRepository) {
        this.receiptRepository = receiptRepository;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createReceiptByUser(String username, ReceiptDto dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (dto.categoryName() == null) {
            throw new BusinessException("Deve ser informado a categoria.");
        }

        Category category = categoryService.findOrCreateByUser(dto.categoryName(), TransactionType.RECEIPT, username);

        Receipt receipt = ReceiptMapper.toEntity(dto, category, user);

        receiptRepository.save(receipt);
    }

    @Transactional(readOnly = true)
    public ReceiptDto findByIdAndUser(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        if (id == null) {
            throw new BusinessException("O id não pode ser nulo.");
        }

        return receiptRepository.findByIdAndUserId(id, user.getId())
                .map(ReceiptMapper::toDto)
                .orElseThrow(() ->
                        new BusinessException("Receita não encontrada com o id: " + id));
    }
}
