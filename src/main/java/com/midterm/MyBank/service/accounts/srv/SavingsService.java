package com.midterm.MyBank.service.accounts.srv;

import com.midterm.MyBank.model.accounts.Savings;

public interface SavingsService {
    Savings get(long id);
    Savings save(Savings savings);
    Savings update(Savings savings, long id);

    void yearlyInterestApplied(Long id);
    void delete(Savings savings);
}
