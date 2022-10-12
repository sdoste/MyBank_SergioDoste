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
            @AttributeOverride( name = "currency", column = @Column(name = "checking_account_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "checking_account_amount"))
    })
    private Money minimumBalance;
    protected LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    protected Status status;
    protected String secretKey;
    private BigDecimal monthlyMaintenanceFee;
    private final BigDecimal defaultMinBalance = new BigDecimal("250");
    private final BigDecimal defaultMonthlyMaintenanceFee = new BigDecimal("12");


    //constructors
    public Checking() {
    }

    public Checking(String secretKey, AccountHolder primaryOwner) {
        super(primaryOwner);
        this.secretKey = secretKey;
        this.minimumBalance = new Money(defaultMinBalance);
        this.monthlyMaintenanceFee = defaultMonthlyMaintenanceFee;
        this.status = Status.ACTIVE;
        this.creationDate = LocalDate.now();
    }

    public Money getBalance() {
        return this.balance;
    }
    //setters & getters
    public void setBalance(Money balance) {
        BigDecimal amount = balance.getAmount();
        if (amount.compareTo(minimumBalance.getAmount()) >= 0) {
            this.balance = balance;
            //if balance drops below minimum balance, penaltyFee is applied
        } else {
          this.balance = new Money(this.balance.decreaseAmount(penaltyFee));
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
