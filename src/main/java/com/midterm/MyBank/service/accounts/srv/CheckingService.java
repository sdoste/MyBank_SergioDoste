package com.midterm.MyBank.service.accounts.srv;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;

public interface CheckingService {
    Checking get(long id);
    Account save(Checking checkingAccount);
    Checking update(Checking checkingAccount, long id);
    Checking transfer(long userId, long recipientId, Money money);
    Account getAccount(long id);
    boolean enoughFunds(long userId, Money money);
    void delete(Checking checkingAccount);
}
