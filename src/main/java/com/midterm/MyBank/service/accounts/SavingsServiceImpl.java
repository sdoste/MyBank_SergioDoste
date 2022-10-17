package com.midterm.MyBank.service.accounts;

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
        return savingsRepository.findById(id).get();
    }

    @Override
    public Savings save(SavingsDTO savingsDTO) {
//        try {
            if (userRepository.findById(savingsDTO.getPrimaryOwnerId()).isPresent()) {
                AccountHolder primaryOwner = (AccountHolder) userRepository.findById(savingsDTO.getPrimaryOwnerId()).get();
                Savings newSavingsAccount = new Savings(savingsDTO.getSecretKey(), primaryOwner,
                        savingsDTO.getInterestRate(), savingsDTO.getMinimumBalance(), savingsDTO.getBalance());
                savingsRepository.save(newSavingsAccount);
                if (savingsRepository.findById(newSavingsAccount.getId()).isPresent()){
                    throw new ResponseStatusException(HttpStatus.OK, "Savings account created");
                } else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Savings account apparently not created");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect primary Owner Id");
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving the Checking account");
//        }
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
