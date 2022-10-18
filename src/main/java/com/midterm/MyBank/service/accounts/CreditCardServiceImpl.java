package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.controller.CreditCardController;
import com.midterm.MyBank.controller.dto.CreditCardDTO;
import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import com.midterm.MyBank.service.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

@Service
public class CreditCardServiceImpl extends CreditCardController implements CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;
    //component with methods to transfer, find accounts (any type) and check if enough funds
    @Autowired
    AccountActions accountActions;
    @Autowired
    UserRepository userRepository;

    @Override
    public CreditCard get(String username, long id) {
        applyMonthlyInterestRate(id);
        return creditCardRepository.findById(id).get();
    }

    @Override
    public CreditCard save(CreditCardDTO creditCardDTO) {
        try {
        if (userRepository.findById(creditCardDTO.getPrimaryOwnerId()).isPresent()) {
            AccountHolder primaryOwner = (AccountHolder) userRepository.findById(creditCardDTO.getPrimaryOwnerId()).get();
            CreditCard newCreditCardAccount = new CreditCard(creditCardDTO.getBalance(), primaryOwner,
                    creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRate());
            return creditCardRepository.save(newCreditCardAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect primary Owner Id");
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving the Checking account");
        }
    }

    @Override
    public CreditCard updateLastAppliedInterestRate(LocalDate newDate, long id) {
        CreditCard recoveredAccount = creditCardRepository.findById(id).get();
        recoveredAccount.setLastAppliedInterestDate(newDate);
        creditCardRepository.save(recoveredAccount);
        return recoveredAccount;
    }

    @Override
    public CreditCard transfer(long userId, long recipientId, Money money){
        CreditCard creditCardAccount = creditCardRepository.findById(userId).get();
        //AA checks if recipientId valid, if enough funds in account, and adds money
        if (accountActions.transferred(userId, recipientId, money)){
            //so money has been added to another account, so it's subtracted from this account
            creditCardAccount.setBalance(new Money(creditCardAccount.getBalance().decreaseAmount(money)));
            applyMonthlyInterestRate(userId);
            return creditCardRepository.save(creditCardAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while transferring money");
        }
    }

    @Override
    public void applyMonthlyInterestRate(long id){
        CreditCard creditCard = creditCardRepository.findById(id).get();
        int DaysSinceLastApplied = (int) Duration.between(creditCard.getLastAppliedInterestDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        //if more than 1 month (30 days) has passed since last time interest was applied, add appropriate interest
        if (DaysSinceLastApplied > 30){
            //Yearly interest x balance = Interest for the whole year
            BigDecimal yearInterest = creditCard.getInterestRate().multiply(creditCard.getBalance().getAmount());
            //Interest whole year / 365 = interest per day
            BigDecimal amountPerDay = yearInterest.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP);
            //Interest per day x number of days passed = increasedbalance
            BigDecimal amountToBeIncreased = amountPerDay.multiply(BigDecimal.valueOf(DaysSinceLastApplied));
            //adding and saving balance
            Money IncreasedBalance = new Money(creditCard.getBalance().increaseAmount(amountToBeIncreased));
            creditCard.setBalance(IncreasedBalance);
            creditCard.setLastAppliedInterestDate(LocalDate.now());
            creditCardRepository.save(creditCard);
        }
    }

    @Override
    public void delete(CreditCard savingsAccount) {
        try {
            creditCardRepository.delete(savingsAccount);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting savings account");
        }
    }
}
