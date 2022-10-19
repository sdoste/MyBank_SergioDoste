package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditCard extends Account {
    public static void main(String[] args) {
    }

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "balance_amount"))
    })
    protected Money balance;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "credit_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "credit_amount"))
    })
    protected Money creditLimit;
    @Column(name = "interest_rate", precision = 19, scale = 4)
    protected BigDecimal interestRate;

    protected LocalDate lastAppliedInterestDate;
    private final BigDecimal defaultCreditLimit = new BigDecimal("100");
    private final BigDecimal defaultInterestRate = new BigDecimal("0.2");
    //constructors
    public CreditCard(){

    }
    public CreditCard(Money balance, AccountHolder primaryOwner, BigDecimal creditLimit, BigDecimal interestRate){
        //if balance not given, 0 by default
        super(primaryOwner);
        if (balance == null){
            this.balance = new Money(new BigDecimal("0.00"));
        } else{
            this.balance = balance;
        }
        //if no credit limit is provided, default is 100
        if (creditLimit == null){
            this.creditLimit = new Money(defaultCreditLimit);
        } else{
            setCreditLimit(creditLimit);
        }
        if (interestRate == null){
            this.creditLimit = new Money(defaultCreditLimit);
        } else{
            setInterestRate(interestRate);
        }
        this.lastAppliedInterestDate = LocalDate.now();
    }

    //setters and getters
    public void setCreditLimit(BigDecimal creditLimit){
        BigDecimal maxCreditLimit = new BigDecimal("100000");
        //if credit limit bigger or equal to 100k, set it to 100k
        if (creditLimit.compareTo(maxCreditLimit) >= 0){
            this.creditLimit = new Money(maxCreditLimit);
            //if interestRater less or equal than 100, set it to 100
        } else if (creditLimit.compareTo(defaultCreditLimit) <= 0){
            this.creditLimit = new Money(defaultCreditLimit);
        } else {
            this.creditLimit = new Money(creditLimit);
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        BigDecimal minRate = new BigDecimal("0.1");
        //if interestRate less or equal to 0.1, set it to 0.1
        if (interestRate.compareTo(minRate) <= 0){
            this.interestRate = minRate;
            //if interestRater bigger or equal than 0.2, set it to 0.2
        } else if (interestRate.compareTo(defaultInterestRate) >= 0){
            this.interestRate = defaultInterestRate;
            //rest of cases (0.1 -> 0.2) are correct, normal setter
        } else {
            this.interestRate = interestRate;
        }
    }
    // setters & getters
    public Money getBalance() {
        return this.balance;
    }
    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return this.primaryOwner;
    }


    public Money getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public Money getPenaltyFee() {
        return this.penaltyFee;
    }

    public void setPenaltyFee(Money penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public LocalDate getLastAppliedInterestDate() {
        return this.lastAppliedInterestDate;
    }

    public void setLastAppliedInterestDate(LocalDate lastAppliedInterestDate) {
        this.lastAppliedInterestDate = lastAppliedInterestDate;
    }

}
