package com.midterm.MyBank.service.users.utils;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.*;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AccountActions {
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    UserRepository userRepository;

    public Account find(long id){
        if (checkingRepository.findById(id).isPresent()){
            return checkingRepository.findById(id).get();
        } else if (studentCheckingRepository.findById(id).isPresent()) {
            return studentCheckingRepository.findById(id).get();
        } else if (creditCardRepository.findById(id).isPresent()){
            return creditCardRepository.findById(id).get();
        } else if (savingsRepository.findById(id).isPresent()) {
            return savingsRepository.findById(id).get();
        } else if (savingsRepository.findById(id).isPresent()) {
            return savingsRepository.findById(id).get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account holder id is invalid");
        }
    }
    public Account transfer(long userId, long recipientId, Money money){
        Account issuingAccount = find(userId);
        if (!(find(recipientId) == null)){
            //Recipient Id is VALID
            if (enoughFunds(userId, money)){
                //ENOUGH FUNDS
                Account recipientAccount = find(recipientId);
                issuingAccount.setBalance(new Money(issuingAccount.getBalance().decreaseAmount(money)));
                recipientAccount.setBalance(new Money(recipientAccount.getBalance().increaseAmount(money)));
            } else
                //NOT ENOUGH FUNDS
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds in your account");
        } else  {
            //Recipient Id INVALID
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong recipient Id");
        }
        return issuingAccount;
    }
//    public Account transfer(long userId, long recipientId, Money money){
//        Account recoveredAccount = find(userId);
//        if (find(recipientId) == null){
//            //Recipient Id INVALID
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong recipient Id");
//        } else if (enoughFunds(userId, money)) {
//            //ENOUGH FUNDS
//            switch (find(userId).getClass().getSimpleName()) {
//                case "Checking":
//                    Checking checkingAccount = (Checking) find(userId);
//                    recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(money)));
//                    checkingAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(money)));
//                break;
//                case "StudentChedking":
//                    StudentChecking studentCheckingAccount = (StudentChecking) find(userId);
//                    recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(money)));
//                    studentCheckingAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(money)));
//                break;
//                case "Savings":
//                    Savings savingsAccount = (Savings) find(userId);
//                    recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(money)));
//                    savingsAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(money)));
//                break;
//                case "CreditCard":
//                    CreditCard creditCardAccount = (CreditCard) find(userId);
//                    recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(money)));
//                    creditCardAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(money)));
//                break;
//            }
//        } else {
//            //NOT ENOUGH FUNDS
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds in your account");
//        }
//        return recoveredAccount;
//    }
    public Account transferToThirdParty(long userId, long thirdPartyId, Money money){
        Account issuingAccount = find(userId);
        if (userRepository.findById(thirdPartyId).isPresent()){
            //Recipient Id is VALID
            if (enoughFunds(userId, money)){

                issuingAccount.setBalance(new Money(issuingAccount.getBalance().increaseAmount(money)));
            } else
                //NOT ENOUGH FUNDS
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds in your account");
        } else  {
            //Recipient Id INVALID
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong recipient Id");
        }
        return issuingAccount;
    }
    public boolean enoughFunds(long accountId, Money money){
        Account recoveredAccount = find(accountId);
        if (recoveredAccount.getBalance().getAmount().compareTo(money.getAmount()) > 0){
            return false;
        } else{
            return true;
        }
    }
}
