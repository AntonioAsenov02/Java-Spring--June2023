package com.example.springbootdemo.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    private Long id;
    private String username;
    private int age;
    private List<Account> accounts;

    public User() {
        this.accounts = new ArrayList<>();
    }

    public User(String username, int age, Account account) {
        this();

        this.username = username;
        this.age = age;
        this.accounts.add(account);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
