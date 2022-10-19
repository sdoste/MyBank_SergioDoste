package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;
import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    @Test
    void setCreditLimit() {
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120");
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        AccountHolder userFran = new AccountHolder("fran", encryptPassword("fran123"), "Francisco Tilleo", date1992, address);
        BigDecimal creditLimit = new BigDecimal("0.5");
        BigDecimal interestRate = new BigDecimal("1111");

        //if null interestRate and credit limit
        CreditCard userFranAccount = new CreditCard(new Money(new BigDecimal("1111")), userFran,null, null);
        assertEquals(new BigDecimal("100.00"), userFranAccount.getCreditLimit().getAmount());
        assertEquals(new BigDecimal("0.2"), userFranAccount.getInterestRate());
    }

    @Test
    void setInterestRate() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void setBalance() {
    }

    @Test
    void testSetCreditLimit() {
    }

    @Test
    void getPenaltyFee() {
    }

    @Test
    void setPenaltyFee() {
    }
}