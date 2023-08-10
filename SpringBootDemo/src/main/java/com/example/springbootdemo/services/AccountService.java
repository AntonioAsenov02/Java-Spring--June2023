package com.example.springbootdemo.services;

import java.math.BigDecimal;

public interface AccountService {

    void withdrawMoney(BigDecimal amount, Long id);
    void depositMoney(BigDecimal amount, Long id);
}
