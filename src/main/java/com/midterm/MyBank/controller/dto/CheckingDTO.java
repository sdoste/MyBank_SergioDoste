package com.midterm.MyBank.controller.dto;
import com.midterm.MyBank.model.Utils.Money;



public class CheckingDTO {
//    @NotEmpty(message = "Employee can't be empty or null.")
    private Long primaryOwnerId;
//    @NotEmpty(message = "Name can't be empty or null.")
    private Money balance;
//    @NotEmpty(message = "Department can't be empty or null.")
    private String secretKey;
//    @NotEmpty(message = "Status can't be empty or null.")

    public CheckingDTO() {
    }

    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
