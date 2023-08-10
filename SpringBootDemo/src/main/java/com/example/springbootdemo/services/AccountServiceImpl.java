package com.example.springbootdemo.services;

import com.example.springbootdemo.models.Account;
import com.example.springbootdemo.repositories.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public void withdrawMoney(BigDecimal amount, Long id) {

        Optional<Account> account = this.accountRepository.findById(id);

        if (account.isEmpty()){
            throw new RuntimeException("Invalid account");
        }

        BigDecimal currentBalance = account.get().getBalance();

        if (amount.compareTo(currentBalance) > 0){
            throw new RuntimeException("Not enough balance");
        }

        account.get().setBalance(currentBalance.subtract(amount));

        this.accountRepository.save(account.get());

    }

    @Override
    public void depositMoney(BigDecimal amount, Long id) {

        Optional<Account> account = this.accountRepository.findById(id);

        if (account.isEmpty()){
            throw new RuntimeException("Invalid account");

        }

        BigDecimal currentBalance = account.get().getBalance();

        account.get().setBalance(currentBalance.add(amount));

        this.accountRepository.save(account.get());
    }
}
