package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.Utils.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@MappedSuperclass
public class Account {

    @Id
    @GeneratedValue
    protected long id;
    @Embedded
    protected Money balance;
    @ManyToOne
    protected AccountHolder PrimaryOwner;
    @ManyToOne
    protected AccountHolder SecondaryOwner;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "penalty_fee_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "penalty_fee_amount"))
    })
    protected Money penaltyFee = new Money(new BigDecimal("40"));
    @Enumerated(EnumType.STRING)
    protected Status status;

    //constructors
    public Account() { }

    //constructor with & primaryOwner
    protected Account(AccountHolder primaryOwner) {
        this.PrimaryOwner = primaryOwner;
    }
    //constructor with primaryOwner & secondary owner
    protected Account(AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.PrimaryOwner = primaryOwner;
        this.SecondaryOwner = secondaryOwner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountHolder getPrimaryOwner() {
        return PrimaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        PrimaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return SecondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        SecondaryOwner = secondaryOwner;
    }

    public Money getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(Money penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



}
