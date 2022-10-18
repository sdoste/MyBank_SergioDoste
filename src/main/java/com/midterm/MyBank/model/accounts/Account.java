package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.Utils.Status;

import javax.persistence.*;
import java.math.BigDecimal;

@MappedSuperclass
public class Account {

    @Id
    @GeneratedValue
    protected long id;
    @Embedded
    protected Money balance;
    @ManyToOne
    protected AccountHolder primaryOwner;
    @ManyToOne
    protected AccountHolder secondaryOwner;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "penalty_fee_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "penalty_fee_amount"))
    })
    protected Money penaltyFee = new Money(new BigDecimal("40"));

    //constructors
    public Account() { }

    //constructor with & primaryOwner
    protected Account(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }
    //constructor with primaryOwner & secondary owner
    protected Account(AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryowner) {
        this.secondaryOwner = secondaryowner;
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

}
