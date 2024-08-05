package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;
    private String icon;
    private String note;

    @OneToMany(mappedBy = "categories")
    private Collection<Transaction> transaction;

    public Collection<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(Collection<Transaction> transaction) {
        this.transaction = transaction;
    }
}
