package com.inge.accounts.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_expenses")
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private int installment;

    @Column(nullable = false)
    private int totalInstallments;

    @Column(nullable = false)
    private boolean payment;

    @Column
    private LocalDate paymentDate;

    @Column
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getInstallment() { return installment; }

    public void setInstallment(int installment) { this.installment = installment; }

    public int getTotalInstallments() { return totalInstallments; }

    public void setTotalInstallments(int totalInstallments) { this.totalInstallments = totalInstallments;}

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public LocalDate getPaymentDate() {return paymentDate;}

    public void setPaymentDate(LocalDate paymentDate) {this.paymentDate = paymentDate;}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
