package com.midterm.MyBank.controller.dto;

import com.midterm.MyBank.model.Utils.Money;

public class AccountDTO {
    private long id;
    private String accountType;
    private Money accountBalance;

    public AccountDTO(long id, String accountType, Money accountBalance) {
        this.id = id;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Money getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Money accountBalance) {
        this.accountBalance = accountBalance;
    }
}
