package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import com.midterm.MyBank.service.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;
    //component with methods to transfer, find accounts (any type) and check if enough funds
    @Autowired
    AccountActions accountActions;

    @Override
    public CreditCard get(String username, long id) {
        if (creditCardRepository.findById(id).isPresent()){
            //account exists
            return creditCardRepository.findById(id).get();
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no Credit Card Account with this id");
        }
    }

    @Override
    public CreditCard save(CreditCard creditCardAccount) {
        try {
            return creditCardRepository.save(creditCardAccount);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving the Credit card account");
        }
    }

    @Override
    public CreditCard update(CreditCard creditCardAccount, long id) {
        if (creditCardRepository.findById(id).isPresent()){
            //account exists
            CreditCard recoveredAccount = creditCardRepository.findById(id).get();
            recoveredAccount.setBalance(creditCardAccount.getBalance());
            return creditCardRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }


    @Override
    public CreditCard transfer(long userId, long recipientId, Money money){
        //checking if issuing account exists
        if (creditCardRepository.findById(userId).isPresent()){
            //checking if account is a Credit Card
            if (accountActions.find(userId).getClass().getSimpleName() == "CreditCard"){
                //accountActions checks if recipientId is valid, if enough funds in user account, and does the transfer
                return (CreditCard) accountActions.transfer(userId, recipientId, money);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be from a credit card Account");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreditCard Id is invalid");
        }
    }

    @Override
    public void delete(CreditCard creditCardAccount) {
        try {
            creditCardRepository.delete(creditCardAccount);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting account");
        }
    }
}
