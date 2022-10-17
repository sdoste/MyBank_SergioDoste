package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.controller.dto.SavingsDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Savings;

public interface SavingsService {
    Savings get(String username, long id);
    Savings save(SavingsDTO savingsDTO);
    Savings update(Savings savingsAccount, long id);
    Savings transfer(long userId, long recipientId, Money money);

    void delete(Savings savingsAccount);
}
