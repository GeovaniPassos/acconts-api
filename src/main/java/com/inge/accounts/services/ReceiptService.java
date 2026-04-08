package com.inge.accounts.services;

import com.inge.accounts.domain.dto.ReceiptDto;
import com.inge.accounts.domain.dto.ReceiptPatchDto;
import com.inge.accounts.domain.dto.ReceiptSearchResponseDto;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.entity.Receipt;
import com.inge.accounts.domain.entity.User;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.ReceiptMapper;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.ReceiptRepository;
import com.inge.accounts.repository.UserRepository;
import com.inge.accounts.specification.ExpensesSpecification;
import com.inge.accounts.specification.ReceiptSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    private final ExpensesService expensesService;

    @PersistenceContext
    private EntityManager entityManager;

    public ReceiptService(ReceiptRepository receiptRepository,
                           CategoryService categoryService,
                           UserRepository userRepository,
                          ExpensesService expensesService) {
        this.receiptRepository = receiptRepository;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
        this.expensesService = expensesService;
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

    @Transactional
    public void deleteByUser(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Receipt receipt = receiptRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new BusinessException("Receita não encontrada!"));
        receiptRepository.delete(receipt);
    }

    @Transactional
    public void patchByUser(Long id, ReceiptPatchDto dto, String username) {

        if (id == null) {
            throw new BusinessException("O id não pode ser nulo.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Receipt receipt = receiptRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new BusinessException(
                        "Receita não encontrada para edição."));

        if (dto.name() != null) {
            receipt.setName(dto.name());
        }

        if (dto.description() != null) {
            receipt.setDescription(dto.description());
        }

        if (dto.categoryName() != null) {
            Category category = categoryService.findOrCreateByUser(
                    dto.categoryName(), TransactionType.RECEIPT, username);
            receipt.setCategory(category);
        }

        if (dto.value() != null) {
            receipt.setValue(dto.value());
        }

        if (dto.date() != null) {
            receipt.setDate(dto.date());
        }

        ReceiptMapper.toDto(receipt);
    }

    @Transactional
    public ReceiptSearchResponseDto findReceiptByUser(LocalDate startDate, LocalDate endDate, String name, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Specification<Receipt> spec = ReceiptSpecification.filter(user.getId(), startDate, endDate, name);

        List<ReceiptDto> list = receiptRepository.findAll(spec, Sort.by("date", "name"))
                .stream()
                .map(ReceiptMapper::toDto)
                .toList();

        BigDecimal total = sumTotal(spec);

        Specification<Expenses> specExpenses = ExpensesSpecification.filter(user.getId(), startDate, endDate, name);
        Specification<Expenses> paidSpec = specExpenses.and((root, query, cb) -> cb.isTrue(root.get("payment")));
        BigDecimal totalPaid = expensesService.sumTotal(paidSpec);

        BigDecimal totalRemaining = total.subtract(totalPaid);

        return new ReceiptSearchResponseDto(list, total, totalRemaining);

    }

    public BigDecimal sumTotal(Specification<Receipt> spec) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Receipt> root = query.from(Receipt.class);

        Predicate predicate = spec.toPredicate(root, query, cb);

        query.select(
                cb.coalesce(cb.sum(root.get("value")), BigDecimal.ZERO)
        );

        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }


}
