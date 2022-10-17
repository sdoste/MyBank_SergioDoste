package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.Utils.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Checking extends Account {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    private Money minimumBalance;
    protected LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    protected Status status;
    protected String secretKey;
    private BigDecimal monthlyMaintenanceFee;


    //constructors
    public Checking() {
    }

    public Checking(String secretKey, AccountHolder primaryOwner, Money balance) {
        super(primaryOwner);
        this.secretKey = secretKey;
        this.monthlyMaintenanceFee = new BigDecimal("12");
        this.creationDate = LocalDate.now();
        this.minimumBalance = new Money(new BigDecimal("250"));
        this.balance = balance;
        setBalance(balance);
        this.status = Status.ACTIVE;
    }

    public Money getBalance() {
        return this.balance;
    }
    //setters & getters
    @Override
    public void setBalance(Money balance) {
        BigDecimal amount = balance.getAmount();
        if (amount.compareTo(minimumBalance.getAmount()) >= 0) {
            this.balance = balance;
            //if balance drops below minimum balance, penaltyFee is applied on new balance
        } else {
            this.balance = new Money(balance.decreaseAmount(penaltyFee));
        }
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
