package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.service.accounts.interfaces.SavingsService;
import com.midterm.MyBank.service.users.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SavingsServiceImpl implements SavingsService {
    @Autowired
    SavingsRepository savingsRepository;
    //component with methods to transfer, find accounts (any type) and check if enough funds
    @Autowired
    AccountActions accountActions;

    @Override
    public Savings get(String username, long id) {
        if (savingsRepository.findById(id).isPresent()){
            //account exists
            return savingsRepository.findById(id).get();
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no Savings Account with this id");
        }
    }

    @Override
    public Savings save(Savings savingsAccount) {
        try {
            return savingsRepository.save(savingsAccount);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving the Savings Account");
        }
    }

    @Override
    public Savings update(Savings savingsAccount, long id) {
        if (savingsRepository.findById(id).isPresent()){
            //account exists
            Savings recoveredAccount = savingsRepository.findById(id).get();
            recoveredAccount.setBalance(savingsAccount.getBalance());
            return savingsRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saving account does not exist");
        }
    }

    @Override
    public Savings transfer(long userId, long recipientId, Money money){
        //checking if issuing account exists
        if (savingsRepository.findById(userId).isPresent()){
            //checking if account is a Credit Card
            if (accountActions.find(userId).getClass().getSimpleName() == "Savings"){
                //accountActions checks if recipientId is valid, if enough funds in user account, and does the transfer
                return (Savings) accountActions.transfer(userId, recipientId, money);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be from a savings account");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Savings Account Id is invalid");
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
