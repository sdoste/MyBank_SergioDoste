package com.midterm.MyBank.model.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CheckingTest {


    @Test
    void setBalance() {
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120");
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        AccountHolder userFran = new AccountHolder("fran", encryptPassword("fran123"), "Francisco Tilleo", date1992, address);
        Checking userFranAccount = new Checking("123", userFran, new Money(new BigDecimal("1111")));


        userFranAccount.setBalance(new Money(new BigDecimal("250")));
        assertEquals(new BigDecimal("250.00"), userFranAccount.getBalance().getAmount());
        //penalty fee applies if less than 250
        userFranAccount.setBalance(new Money(new BigDecimal("240")));
        assertEquals(new BigDecimal("200.00"), userFranAccount.getBalance().getAmount());
    }

}