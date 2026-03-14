package com.inge.accounts.services;

import com.inge.accounts.domain.dto.*;
import com.inge.accounts.domain.entity.Category;
import com.inge.accounts.domain.entity.Expenses;
import com.inge.accounts.domain.enums.TransactionType;
import com.inge.accounts.domain.mapper.CategoryMapper;
import com.inge.accounts.domain.mapper.ExpensesMapper;
import com.inge.accounts.exceptions.BusinessException;
import com.inge.accounts.repository.ExpensesRepository;
import com.inge.accounts.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static com.inge.accounts.domain.mapper.ExpensesMapper.toAddInstallmentsDto;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final CategoryService categoryService;

    public ExpensesService(ExpensesRepository expensesRepository,
                           CategoryService categoryService) {
        this.expensesRepository = expensesRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public void createExpenses(ExpensesDto dto) {

        //Se a despesa já existir, inserir parcela
        if (!expensesRepository.findByName(dto.name()).isEmpty()) {
            addInstallments(toAddInstallmentsDto(dto));
            return;
        }

        if (dto.categoryName() == null) {
            throw new BusinessException("Deve ser informado a categoria.");
        }

        Category category = categoryService.findOrCreate(dto.categoryName(), TransactionType.EXPENSES);

        LocalDate baseDate = dto.date();
        int total = dto.totalInstallments();

        for(int i = 1; i <= total ;i++) {

            Expenses expense = ExpensesMapper.toEntity(dto, category);

            expense.setInstallment(i);
            expense.setTotalInstallments(total);

            LocalDate installmentDate = baseDate.plusMonths(i - 1);
            expense.setDate(installmentDate);

            expensesRepository.save(expense);
        }
    }

    @Transactional
    public void addInstallments(ExpensesAddInstallmentsDto dto) {

        List<Expenses> expenses = expensesRepository.findByName(dto.name());

        Expenses lastExpense =  expenses.stream()
                .max(Comparator.comparing(Expenses::getInstallment))
                .orElseThrow(() -> new BusinessException("Despensa não encontrada"));

        LocalDate lastDate = lastExpense.getDate();
        int newTotalInstallments = lastExpense.getTotalInstallments() + dto.installments();

        expenses.forEach(exp -> exp.setTotalInstallments(newTotalInstallments));

        for(int i = 1; i <= dto.installments(); i++){
            Expenses newExpense = ExpensesMapper.copyExpensesAddInstallments(lastExpense, newTotalInstallments);

            newExpense.setInstallment(lastExpense.getInstallment() + i);
            if (dto.value() != null) newExpense.setValue(dto.value());
            newExpense.setDate(lastDate.plusMonths(i));

            expensesRepository.save(newExpense);
        }
    }

    @Transactional(readOnly = true)
    public ExpenseSearchResponseDto findAll() {
        List<ExpensesDto> list = expensesRepository.findAll()
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new BusinessException("A lista de despesas está vazia.");
        }

        LocalDate startDate = null;
        LocalDate endDate = null;
        String name = null;

        BigDecimal total = expensesRepository.sumValueTotalExpenses(startDate, endDate, name);
        BigDecimal totalPaid = expensesRepository.sumValueTotalPaidExpenses(startDate, endDate, name);
        BigDecimal totalUnpaid = expensesRepository.sumValueTotalUnpaidExpenses(startDate, endDate, name);

        return new ExpenseSearchResponseDto(list, total, totalPaid, totalUnpaid);
    }

    @Transactional(readOnly = true)
    public ExpensesDto findById(Long id) {

        if (id == null) {
            throw new BusinessException("O id não pode ser nulo.");
        }

        return expensesRepository.findById(id)
                .map(ExpensesMapper::toDto).orElseThrow(() ->
                        new BusinessException("Despesa não encontrada com o id: " + id));
    }

    @Transactional
    public void delete(Long id) {
        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Despesa não encontrada!"));
        expensesRepository.delete(expenses);
    }

    @Transactional
    public void patch(Long id, ExpensesPatchDto dto) {
        if (id == null) {
            throw new BusinessException("O id não pode ser nulo.");
        }

        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Despesa não encontrada para edição."));

        if (dto.name() != null) {
            expenses.setName(dto.name());
        }

        if (dto.description() != null) {
            expenses.setDescription(dto.description());
        }

        if (dto.categoryName() != null) {
            Category category = categoryService.findOrCreate(
                    dto.categoryName(), TransactionType.EXPENSES);
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
    public void togglePayment(Long id) {
        if (id == null) {
            throw new BusinessException("Id não pode ser nulo.");
        }

        Expenses expense = expensesRepository.findById(id)
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

    @Transactional(readOnly = true)
    public List<ExpensesDto> findByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("Data inicial não pode ser maior que a final.");
        }

        List<ExpensesDto> list = expensesRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new BusinessException("Nenhuma despesa encontrada no periodo");
        }

        return list;
    }

    @Transactional(readOnly = true)
    public List<ExpensesDto> findByMonth(YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<ExpensesDto> list = expensesRepository.findByDateBetween(start, end)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new BusinessException("Nenhuma despesa encontrada no periodo.");
        }

        return list;
    }

    @Transactional(readOnly = true)
    public List<ExpensesDto> findByName(String name) {
        if (StringUtils.isNullOrBlank(name)) {
            throw new BusinessException("O nome não pode ser nulo na pesquisa!");
        }

        List<ExpensesDto> list = expensesRepository.findByNameContainsIgnoreCase(name)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new BusinessException("Nenhuma despesa encontrada com o nome.");
        }

        return list;
    }

    public ExpenseSearchResponseDto findExpenses(LocalDate startDate, LocalDate endDate, String name) {

        List<ExpensesDto> list = expensesRepository.findExpenses(startDate, endDate, name)
                .stream()
                .map(ExpensesMapper::toDto)
                .toList();

        BigDecimal total = expensesRepository.sumValueTotalExpenses(startDate, endDate, name);
        BigDecimal totalPaid = expensesRepository.sumValueTotalPaidExpenses(startDate, endDate, name);
        BigDecimal totalUnpaid = expensesRepository.sumValueTotalUnpaidExpenses(startDate, endDate, name);

        return new ExpenseSearchResponseDto(list, total, totalPaid, totalUnpaid);
    }
}
