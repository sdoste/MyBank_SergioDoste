package com.midterm.MyBank.service.accounts.impl;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.service.accounts.srv.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public CreditCard get(long id) {

        return creditCardRepository.findById(id).get();
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public CreditCard update(CreditCard creditCard, long id) {
        CreditCard recoveredObject = creditCardRepository.findById(id).get();
        recoveredObject.setBalance(creditCard.getBalance());
        return creditCardRepository.save(recoveredObject);
    }

    @Override
    public void monthlyInterestApplied(long id){
        if (creditCardRepository.findById(id).isPresent()){
            CreditCard recoveredCreditCard = creditCardRepository.findById(id).get();
            int MonthsFromLastAccess = Period.between(recoveredCreditCard.getLastAppliedInterestDate(), LocalDate.now()).getMonths();
            //if more than 1 year has passed since last time account was access, add appropriate interest
            if (MonthsFromLastAccess > 1){
                BigDecimal amountToBeIncreased = recoveredCreditCard.getInterestRate().multiply(BigDecimal.valueOf(MonthsFromLastAccess));
                Money IncreasedBalance = new Money(recoveredCreditCard.getBalance().increaseAmount(amountToBeIncreased));
                recoveredCreditCard.setBalance(IncreasedBalance);
            }
            recoveredCreditCard.setLastAppliedInterestDate(LocalDate.now());
        } else {
            System.out.println("Id not valid");
        }
    }
    @Override
    public void delete(CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }
}
