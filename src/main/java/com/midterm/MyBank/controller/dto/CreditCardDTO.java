package com.midterm.MyBank.controller.dto;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;

import java.math.BigDecimal;

public class CreditCardDTO {
    private Money balance;
    private long primaryOwnerId;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;

    public CreditCardDTO(Money balance, long primaryOwnerId, BigDecimal creditLimit, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(long primaryOwner) {
        this.primaryOwnerId = primaryOwner;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
