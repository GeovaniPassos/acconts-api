package com.inge.accounts.entity;

import com.inge.accounts.enums.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_category", uniqueConstraints = {
        @UniqueConstraint(name = "uk_category_name_type", columnNames = {"name", "type"})
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public Category(String name, TransactionType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category() {
    }
}