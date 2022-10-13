package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.model.accounts.CreditCard;

public interface CreditCardService {
    CreditCard get(long id);
    CreditCard save(CreditCard creditCard);
    CreditCard update(CreditCard creditCard, long id);
    void monthlyInterestApplied(long id);
    void delete(CreditCard creditCard);
}
