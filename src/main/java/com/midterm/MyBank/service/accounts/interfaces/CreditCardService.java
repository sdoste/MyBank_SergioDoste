package com.midterm.MyBank.service.accounts.interfaces;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;

public interface CreditCardService {
    CreditCard get(String username, long id);
    CreditCard save(CreditCard creditCardAccount);
    CreditCard update(CreditCard creditCardAccount, long id);
    CreditCard transfer(long userId, long recipientId, Money money);

    void delete(CreditCard creditCardAccount);
}
