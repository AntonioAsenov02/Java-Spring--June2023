package com.example.springbootdemo.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    private Long id;
    private BigDecimal balance;


    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}
