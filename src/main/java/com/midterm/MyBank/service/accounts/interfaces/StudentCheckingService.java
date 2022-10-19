package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.StudentChecking;

public interface StudentCheckingService {
    StudentChecking get(String username, long id);
    StudentChecking update(StudentChecking studentCheckingAccount, long id);
    StudentChecking modifyBalance(Money newBalance, long id);
    StudentChecking increaseBalance(Money subtractedBalance, long id);
    StudentChecking decreaseBalance(Money subtractedBalance, long id);
    StudentChecking transfer(long userId, long recipientId, Money money);
    void delete(long id);
}
