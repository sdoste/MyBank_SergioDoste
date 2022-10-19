package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.controller.dto.SavingsDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Savings;

import java.time.LocalDate;

public interface SavingsService {
    Savings get(String username, long id);
    Savings save(SavingsDTO savingsDTO);
    Savings updateLastAppliedInterestRate(LocalDate newDate, long id);
    Savings modifyBalance(Money newBalance, long id);
    Savings increaseBalance(Money subtractedBalance, long id);
    Savings decreaseBalance(Money subtractedBalance, long id);
    Savings transfer(long userId, long recipientId, Money money);
    void applyYearInterestRate(Long id);
    void delete(long id);
}
