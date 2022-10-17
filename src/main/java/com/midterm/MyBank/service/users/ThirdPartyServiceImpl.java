package com.midterm.MyBank.service.users;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountActions accountActions;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    SavingsRepository savingsRepository;


    public void deposit(long recipientAccountId, String secretKey, Money money){
        if (!(accountActions.find(recipientAccountId) == null)){
            switch(accountActions.find(recipientAccountId).getClass().getSimpleName()){
                case "Checking":
                    Checking checkingAccount = checkingRepository.findById(recipientAccountId).get();
                   if (Objects.equals(checkingAccount.getSecretKey(), secretKey)){
                       checkingAccount.setBalance(new Money(checkingAccount.getBalance().increaseAmount(money)));
                       checkingRepository.save(checkingAccount);
                       String info = "Deposited " + money.getAmount().toString() + "€ successfully to checking account id " + checkingAccount.getId();
                       throw new ResponseStatusException(HttpStatus.OK, info);
                   }
                    break;
                case "StudentChecking":
                    StudentChecking studentCheckingAccount = studentCheckingRepository.findById(recipientAccountId).get();
                    if (Objects.equals(studentCheckingAccount.getSecretKey(), secretKey)){
                        studentCheckingAccount.setBalance(new Money(studentCheckingAccount.getBalance().increaseAmount(money)));
                        studentCheckingRepository.save(studentCheckingAccount);
                        String info = "Deposited " + money.getAmount().toString() + "€ successfully to student checking account id " + studentCheckingAccount.getId();
                        throw new ResponseStatusException(HttpStatus.OK, info);
                    }
                    break;
                case "Savings":
                    Savings savingsAccount = savingsRepository.findById(recipientAccountId).get();
                    if (Objects.equals(savingsAccount.getSecretKey(), secretKey)){
                        savingsAccount.setBalance(new Money(savingsAccount.getBalance().increaseAmount(money)));
                        savingsRepository.save(savingsAccount);
                        String info = "Deposited " + money.getAmount().toString() + "€ successfully to savings account id " + savingsAccount.getId();
                        throw new ResponseStatusException(HttpStatus.OK, info);
                    }
                    break;
                case "CreditCard":
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Third Parties cannot transfer to credit accounts");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id is incorrect");
        }
    }

    public void withdraw(long recipientAccountId, String secretKey, Money money){
        if (!(accountActions.find(recipientAccountId) == null)){
            switch(accountActions.find(recipientAccountId).getClass().getSimpleName()){
                case "Checking":
                    Checking checkingAccount = checkingRepository.findById(recipientAccountId).get();
                    if (Objects.equals(checkingAccount.getSecretKey(), secretKey)){
                        //account secret key matches
                        if (accountActions.enoughFunds(checkingAccount.getId(), money)){
                            //There are enough funds to perform the withdrawal
                            checkingAccount.setBalance(new Money(checkingAccount.getBalance().decreaseAmount(money)));
                            checkingRepository.save(checkingAccount);
                            String info = "Withdrawn " + money.getAmount().toString() + "€ successfully to account id " + checkingAccount.getId();
                            throw new ResponseStatusException(HttpStatus.OK, info);
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account secret key does not match");
                    }
                case "StudentChecking":
                    StudentChecking studentCheckingAccount = studentCheckingRepository.findById(recipientAccountId).get();
                    if (Objects.equals(studentCheckingAccount.getSecretKey(), secretKey)){
                        //account secret key matches
                        if (accountActions.enoughFunds(studentCheckingAccount.getId(), money)){
                            //There are enough funds to perform the withdrawal
                            studentCheckingAccount.setBalance(new Money(studentCheckingAccount.getBalance().decreaseAmount(money)));
                            studentCheckingRepository.save(studentCheckingAccount);
                            String info = "Withdrawn " + money.getAmount().toString() + "€ successfully to account id " + studentCheckingAccount.getId();
                            throw new ResponseStatusException(HttpStatus.OK, info);
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account secret key does not match");
                    }
                case "Savings":
                    Savings savingsAccount = savingsRepository.findById(recipientAccountId).get();
                    if (Objects.equals(savingsAccount.getSecretKey(), secretKey)){
                        //account secret key matches
                        if (accountActions.enoughFunds(savingsAccount.getId(), money)){
                            //There are enough funds to perform the withdrawal
                            savingsAccount.setBalance(new Money(savingsAccount.getBalance().decreaseAmount(money)));
                            savingsRepository.save(savingsAccount);
                            String info = "Withdrawn " + money.getAmount().toString() + "€ successfully to account id " + savingsAccount.getId();
                            throw new ResponseStatusException(HttpStatus.OK, info);
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account secret key does not match");
                    }
                case "CreditCard":
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Third Parties cannot transfer to credit accounts");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id is incorrect");
        }
    }
}
