package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.controller.dto.CreditCardDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;

import java.time.LocalDate;

public interface CreditCardService {
    CreditCard get(String username, long id);
    CreditCard save(CreditCardDTO creditCardDTO);
    CreditCard updateLastAppliedInterestRate(LocalDate newDate, long id);
    CreditCard transfer(long userId, long recipientId, Money money);
    void applyMonthlyInterestRate(long id);
    void delete(long id);
}
