package com.midterm.MyBank.controller.dto;

import com.midterm.MyBank.model.Utils.Money;

import java.math.BigDecimal;

public class SavingsDTO {

    private String secretKey;
    private long primaryOwnerId;
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
    private Money balance;

    //constructor
    public SavingsDTO(String secretKey, long primaryOwnerId, BigDecimal interestRate, BigDecimal minimumBalance, Money balance) {
        this.secretKey = secretKey;
        this.primaryOwnerId = primaryOwnerId;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.balance = balance;
    }
    public SavingsDTO(){
    };

    //Setters and getters

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getPrimaryOwnerId() {
        return this.primaryOwnerId;
    }

    public void setPrimaryOwnerId(long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }
}
