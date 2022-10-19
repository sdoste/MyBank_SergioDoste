package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;
import static org.junit.jupiter.api.Assertions.*;

class SavingsTest {

    @Test
    void testSetInterestRate() {
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120");
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        AccountHolder userFran = new AccountHolder("fran", encryptPassword("fran123"), "Francisco Tilleo", date1992, address);
        BigDecimal creditLimit = new BigDecimal("5000");
        BigDecimal interestRate = new BigDecimal("1111");
        BigDecimal minimumBalance = new BigDecimal("500");
        Money balance = new Money(new BigDecimal("5000"));

        //if null interestRate, default of 0.0025
        Savings Account1 = new Savings("123", userFran, null, minimumBalance, balance);
        assertEquals(new BigDecimal("0.0025"), Account1.getInterestRate());

        //if interestRate bigger than 0.5, should be set to 0.5
        Savings Account2 = new Savings("123", userFran, new BigDecimal("2"), minimumBalance, balance);
        assertEquals(new BigDecimal("0.5000"), Account2.getInterestRate());
    }

    @Test
    void testMinimumBalance() {
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120");
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        AccountHolder userFran = new AccountHolder("fran", encryptPassword("fran123"), "Francisco Tilleo", date1992, address);
        BigDecimal creditLimit = new BigDecimal("0.5");
        BigDecimal interestRate = new BigDecimal("1111");
        Money balance = new Money(new BigDecimal("5000"));


        //minimum balance set correctly
        BigDecimal minimumBalance = new BigDecimal("500");
        Savings userFranAccount1 = new Savings("123", userFran, interestRate, minimumBalance, balance);
        assertEquals(new BigDecimal("500.00"), userFranAccount1.getMinimumBalance().getAmount());


        //if minimumBalance more than 1000, should be set to 1000
        Savings userFranAccount2 = new Savings("123", userFran, interestRate, new BigDecimal("5000"), balance);
        assertEquals(new BigDecimal("1000.00"), userFranAccount2.getMinimumBalance().getAmount());

        //if minimumBalance less than 100, should be set to 100
        Savings userFranAccount3 = new Savings("123", userFran, interestRate, new BigDecimal("50"), balance);
        assertEquals(new BigDecimal("100.00"), userFranAccount3.getMinimumBalance().getAmount());
    }


    @Test
    void testPenaltyFee() {
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120");
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        AccountHolder userFran = new AccountHolder("fran", encryptPassword("fran123"), "Francisco Tilleo", date1992, address);
        BigDecimal creditLimit = new BigDecimal("0.5");
        BigDecimal interestRate = new BigDecimal("1111");
        Money balance = new Money(new BigDecimal("500"));
        BigDecimal minimumBalance = new BigDecimal("400");
        Savings userFranAccount8 = new Savings("123", userFran, interestRate, minimumBalance, balance);

        //Default penalty fee is 40, is applied if balance less than 250
        userFranAccount8.setBalance(new Money(new BigDecimal("300")));
        assertEquals(new BigDecimal("260.00"), userFranAccount8.getBalance().getAmount());
    }
}