package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.StudentChecking;

public interface StudentCheckingService {
    StudentChecking get(String username, long id);
    StudentChecking update(StudentChecking studentCheckingAccount, long id);
    StudentChecking transfer(long userId, long recipientId, Money money);
    void delete(StudentChecking studentCheckingAccount);
}
