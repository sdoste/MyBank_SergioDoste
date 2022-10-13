package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.service.accounts.interfaces.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class SavingsServiceImpl implements SavingsService {
    @Autowired
    SavingsRepository savingsRepository;


    @Override
    public Savings get(long id) {
        return savingsRepository.findById(id).get();
    }

    @Override
    public Savings save(Savings savings) {
        return savingsRepository.save(savings);
    }

    @Override
    public Savings update(Savings savings, long id) {
        Savings recoveredObject = savingsRepository.findById(id).get();
        recoveredObject.setBalance(savings.getBalance());
        return savingsRepository.save(recoveredObject);
    }
    @Override
    public void yearlyInterestApplied(Long id){
        if (savingsRepository.findById(id).isPresent()){
            Savings recoveredSavingsAcc = savingsRepository.findById(id).get();
            int YearsFromLastAccess = Period.between(recoveredSavingsAcc.getLastAppliedInterest(), LocalDate.now()).getYears();
            //if more than 1 year has passed since last time account was access, add appropriate interest
            if (YearsFromLastAccess > 1){
                BigDecimal amountToBeIncreased = recoveredSavingsAcc.getInterestRate().multiply(BigDecimal.valueOf(YearsFromLastAccess));
                Money IncreasedBalance = new Money(recoveredSavingsAcc.getBalance().increaseAmount(amountToBeIncreased));
                recoveredSavingsAcc.setBalance(IncreasedBalance);
            }
            recoveredSavingsAcc.setLastAppliedInterest(LocalDate.now());
        } else {
            System.out.println("Id not valid");
        }
    }

    @Override
    public void delete(Savings savings) {
        savingsRepository.delete(savings);
    }

}
