package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.controller.dto.CheckingDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;

public interface CheckingService {
    Checking get(String username, long id);
    CheckingDTO test(String hello);
    //we pass a checkingAccount but it converts it to student if age < 24
    Account save(CheckingDTO checkingAccountDTO);
    Checking update(Checking checkingAccount, long id);
    Checking transfer(long userId, long recipientId, Money money);
    void delete(Checking checkingAccount);
}
