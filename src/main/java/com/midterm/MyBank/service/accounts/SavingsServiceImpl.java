package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.controller.SavingsController;
import com.midterm.MyBank.controller.dto.SavingsDTO;
import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.service.accounts.interfaces.SavingsService;
import com.midterm.MyBank.service.utils.AccountActions;
import com.midterm.MyBank.repository.security.UserRepository;
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
public class SavingsServiceImpl implements SavingsService {
    @Autowired
    SavingsRepository savingsRepository;
    //component with methods to transfer, find accounts (any type) and check if enough funds
    @Autowired
    AccountActions accountActions;
    @Autowired
    UserRepository userRepository;

    @Override
    public Savings get(String username, long id) {
        applyYearInterestRate(id);
        return savingsRepository.findById(id).get();
    }

    @Override
    public Savings save(SavingsDTO savingsDTO) {
        try {
            if (userRepository.findById(savingsDTO.getPrimaryOwnerId()).isPresent()) {
                AccountHolder primaryOwner = (AccountHolder) userRepository.findById(savingsDTO.getPrimaryOwnerId()).get();
                Savings newSavingsAccount = new Savings(savingsDTO.getSecretKey(), primaryOwner,
                        savingsDTO.getInterestRate(), savingsDTO.getMinimumBalance(), savingsDTO.getBalance());
                return savingsRepository.save(newSavingsAccount);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect primary Owner Id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving the Savings account");
        }
    }

    @Override
    public Savings updateLastAppliedInterestRate(LocalDate newDate, long id) {
        Savings recoveredAccount = savingsRepository.findById(id).get();
        recoveredAccount.setLastAppliedInterest(newDate);
        savingsRepository.save(recoveredAccount);
        applyYearInterestRate(id);
        return recoveredAccount;
    }

    @Override
    public Savings transfer(long userId, long recipientId, Money money){
        Savings savingsAccount = savingsRepository.findById(userId).get();
        //AA checks if recipientId valid, if enough funds in account, and adds money
        if (accountActions.transferred(userId, recipientId, money)){
            //so money has been added to another account, so it's subtracted from this account
            savingsAccount.setBalance(new Money(savingsAccount.getBalance().decreaseAmount(money)));
            savingsRepository.save(savingsAccount);
            applyYearInterestRate(userId);
            return savingsAccount;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while transferring money");
        }
    }

    @Override
    public void applyYearInterestRate(Long id) {
        if (savingsRepository.findById(id).isPresent()){
            Savings recoveredSavingsAcc = savingsRepository.findById(id).get();
            int DaysSinceLastApplied = (int) Duration.between(recoveredSavingsAcc.getLastAppliedInterest().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();;
            ////if more than 1 year (365 days) has passed since last time interest was applied, add appropriate interest
            if (DaysSinceLastApplied > 365){
                //Yearly interest x balance = Interest for the whole year
                BigDecimal yearInterest = recoveredSavingsAcc.getInterestRate().multiply(recoveredSavingsAcc.getBalance().getAmount());
                //Interest whole year / 365 = interest per day
                BigDecimal amountPerDay = yearInterest.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP);
                //Interest per day x number of days passed = increasedbalance
                BigDecimal amountToBeIncreased = amountPerDay.multiply(BigDecimal.valueOf(DaysSinceLastApplied));
                //adding and saving balance
                Money IncreasedBalance = new Money(recoveredSavingsAcc.getBalance().increaseAmount(amountToBeIncreased));
                recoveredSavingsAcc.setBalance(IncreasedBalance);
                recoveredSavingsAcc.setLastAppliedInterest(LocalDate.now());
                savingsRepository.save(recoveredSavingsAcc);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error while applying interest");
        }
    }

    @Override
    public void delete(Savings savingsAccount) {
        try {
            savingsRepository.delete(savingsAccount);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting savings account");
        }
    }
}
