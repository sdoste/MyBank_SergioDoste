package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.Utils.Status;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
public class StudentChecking extends Account {
    protected String secretKey;
    protected LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    protected Status status;
    public StudentChecking() {
    }
    public StudentChecking(String secretKey, AccountHolder primaryOwner, Money balance){
        super(primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        this.balance = balance;
        this.status = Status.ACTIVE;
    }
    //setters and getters

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setBalance(Money balance){
        this.balance = balance;
    }
}
