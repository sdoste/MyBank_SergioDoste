package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.Utils.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Savings extends Account{
    protected String secretKey;
    protected LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    protected Status status;
    @Column(name = "interest_rate", precision = 19, scale = 4)
    private BigDecimal interestRate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "savings_currency")),
            @AttributeOverride( name = "amount", column = @Column(name = "savings_amount"))
    })
    private Money minimumBalance;
    protected LocalDate lastAppliedInterestDate;
    private final BigDecimal defaultRate = new BigDecimal("0.0025");

    //Saved as BigDecimal for easier comparison in setter and to not clash in db with other Money objects
    private final BigDecimal defaultMinBalance = new BigDecimal("1000");

    //constructors
    public Savings() {
    }
    public Savings(String secretKey, AccountHolder PrimaryOwner, BigDecimal interestRate, BigDecimal minimumBalance, Money balance) {
        super(PrimaryOwner);
        this.secretKey = secretKey;
        this.creationDate = LocalDate.now();
        lastAppliedInterestDate = LocalDate.now();
        if (interestRate == null){
            // 0.0025
            this.interestRate = defaultRate;
        } else {
            setInterestRate(interestRate);
        }
        if (minimumBalance == null){
            // 1000
            this.minimumBalance = new Money (defaultMinBalance);
        } else {
            setMinimumBalance(minimumBalance);
        }
        setBalance(balance);
        this.status = Status.ACTIVE;
    }
//getters and setters


    public Money getMinimumBalance() {
        return minimumBalance;
    }


    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getLastAppliedInterest() {
        return lastAppliedInterestDate;
    }

    public void setLastAppliedInterest(LocalDate lastAppliedInterest) {
        this.lastAppliedInterestDate = lastAppliedInterest;
    }

    public void setInterestRate(BigDecimal interestRate) {
        BigDecimal maxRate = new BigDecimal("0.5");
    //if interestRate bigger or equal to 0.5, set it to 0.5
        if (interestRate.compareTo(maxRate) >= 0){
        this.interestRate = maxRate;
        //if interestRater negative, set it to 0
    } else if (interestRate.compareTo(BigDecimal.ZERO) < 0){
        this.interestRate = new BigDecimal("0.00");
        //rest of cases (0 -> 0.5) are correct, normal setter
    } else {
        this.interestRate = interestRate;
    }
}
    public void setMinimumBalance(BigDecimal givenMinBalance){
        BigDecimal minimum = new BigDecimal("100");
        //if given less than 100, set to 100
        if (givenMinBalance.compareTo(minimum) < 0){
            this.minimumBalance = new Money(minimum);
            //if given more than 1000, set to 1000
        } else if (givenMinBalance.compareTo(defaultMinBalance) > 0){
            this.minimumBalance = new Money(defaultMinBalance);
            //rest of cases (100 -> 1000) are correct, normal setter
        } else {
            this.minimumBalance = new Money(givenMinBalance);
        }
    }

    public void setBalance(Money balance) {
        BigDecimal currentAmount = balance.getAmount();
        if (currentAmount.compareTo(minimumBalance.getAmount()) >= 0) {
            this.balance = balance;
            //if balance drops below minimum balance, penaltyFee is applied
        } else {
            this.balance = new Money(balance.decreaseAmount(penaltyFee));
        }
    }

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

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }
}
