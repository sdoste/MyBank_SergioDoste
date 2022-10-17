package com.midterm.MyBank.controller.dto;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;

import java.math.BigDecimal;

public class CreditCardDTO {
    private Money balance;
    private AccountHolder primaryOwner;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;

}
