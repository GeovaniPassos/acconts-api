package com.inge.accounts.services;

import com.inge.accounts.domain.dto.*;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.entity.User;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.ExpensesMapper;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.ExpensesRepository;
import com.inge.accounts.repository.UserRepository;
import com.inge.accounts.specification.ExpensesSpecification;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.inge.accounts.domain.mapper.ExpensesMapper.toAddInstallmentsDto;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ExpensesService(ExpensesRepository expensesRepository,
                           CategoryService categoryService,
                           UserRepository userRepository) {
        this.expensesRepository = expensesRepository;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createExpenses(String username, ExpensesDto dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        //Se a despesa já existir, inserir parcela
        if (expensesRepository.existsByNameAndUserId(dto.name(), user.getId())) {
            addInstallmentsByUser(toAddInstallmentsDto(dto), username);
            return;
        }

        if (dto.categoryName() == null) {
            throw new BusinessException("Deve ser informado a categoria.");
        }

        Category category = categoryService.findOrCreateByUser(dto.categoryName(), TransactionType.EXPENSES, username);

        LocalDate baseDate = dto.date();
        int total = dto.totalInstallments();

        for(int i = 1; i <= total ;i++) {

            Expenses expense = ExpensesMapper.toEntity(dto, category, user);

            expense.setInstallment(i);
            expense.setTotalInstallments(total);

            LocalDate installmentDate = baseDate.plusMonths(i - 1);
            expense.setDate(installmentDate);

            expensesRepository.save(expense);
        }
    }

    @Transactional
    public void addInstallmentsByUser(ExpensesAddInstallmentsDto dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Specification<Expenses> spec = ExpensesSpecification.filter(user.getId(), null, null, dto.name());
        List<Expenses> expenses =  expensesRepository.findAll(spec);

        Expenses lastExpense =  expenses.stream()
                .max(Comparator.comparing(Expenses::getInstallment))
                .orElseThrow(() -> new BusinessException("Despensa não encontrada"));

        LocalDate lastDate = lastExpense.getDate();
        int newTotalInstallments = lastExpense.getTotalInstallments() + dto.installments();

        expenses.forEach(exp -> exp.setTotalInstallments(newTotalInstallments));

        for(int i = 1; i <= dto.installments(); i++){
            Expenses newExpense = ExpensesMapper.copyExpensesAddInstallments(lastExpense, newTotalInstallments);

            newExpense.setUser(user);

            newExpense.setInstallment(lastExpense.getInstallment() + i);

            if (dto.value() != null) newExpense.setValue(dto.value());
            newExpense.setDate(lastDate.plusMonths(i));

            expensesRepository.save(newExpense);
        }
    }

    @Transactional(readOnly = true)
    public ExpensesDto findByIdAndUser(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        if (id == null) {
            throw new BusinessException("O id não pode ser nulo.");
        }

        return expensesRepository.findByIdAndUserId(id, user.getId())
                .map(ExpensesMapper::toDto).orElseThrow(() ->
                        new BusinessException("Despesa não encontrada com o id: " + id));
    }

    @Transactional
    public void deleteByUser(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado"));

        Expenses expenses = expensesRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new BusinessException("Despesa não encontrada!"));
        expensesRepository.delete(expenses);
    }

    @Transactional
    public void patchByUser(Long id, ExpensesPatchDto dto, String username) {

        if (id == null) {
            throw new BusinessException("O id não pode ser nulo.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Expenses expenses = expensesRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new BusinessException(
                        "Despesa não encontrada para edição."));

        if (dto.name() != null) {
            expenses.setName(dto.name());
        }

        if (dto.description() != null) {
            expenses.setDescription(dto.description());
        }

        if (dto.categoryName() != null) {
            Category category = categoryService.findOrCreateByUser(
                    dto.categoryName(), TransactionType.EXPENSES, username);
            expenses.setCategory(category);
        }

        if (dto.payment() != null) {
            expenses.setPayment(dto.payment());

            if (dto.payment()) {
                LocalDate date = dto.paymentDate() != null ? dto.paymentDate() : LocalDate.now();
                expenses.setPaymentDate(date);
            } else {
                expenses.setPaymentDate(null);
            }
        }

        if (dto.value() != null) {
            expenses.setValue(dto.value());
        }

        if (dto.date() != null) {
            expenses.setDate(dto.date());
        }

        ExpensesMapper.toDto(expenses);
    }

    @Transactional
    public void togglePaymentByUser(Long id, String username) {

        if (id == null) {
            throw new BusinessException("Id não pode ser nulo.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Expenses expense = expensesRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new BusinessException("Despesa não encontrada."));

        boolean newStatus = !expense.isPayment();
        expense.setPayment(newStatus);

        if (newStatus) {
            expense.setPaymentDate(LocalDate.now());
        } else {
            expense.setPaymentDate(null);
        }

        ExpensesMapper.toDto(expense);
    }

    public ExpenseSearchResponseDto findExpensesByUser(LocalDate startDate, LocalDate endDate, String name, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Specification<Expenses> spec = ExpensesSpecification.filter(user.getId(), startDate, endDate, name);

        List<ExpensesDto> list =  expensesRepository.findAll(spec)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();

        BigDecimal total = sumTotal(spec);

        Specification<Expenses> paidSpec = spec.and((root, query, cb) -> cb.isTrue(root.get("payment")));
        BigDecimal totalPaid = sumTotal(paidSpec);

        Specification<Expenses> unpaidSpec = spec.and((root, query, cb) -> cb.isFalse(root.get("payment")));
        BigDecimal totalUnpaid = sumTotal(unpaidSpec);

        return new ExpenseSearchResponseDto(list, total, totalPaid, totalUnpaid);
    }

    public BigDecimal sumTotal(Specification<Expenses> spec) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Expenses> root = query.from(Expenses.class);

        Predicate predicate = spec.toPredicate(root, query, cb);

        query.select(
                cb.coalesce(cb.sum(root.get("value")), BigDecimal.ZERO)
        );

        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
