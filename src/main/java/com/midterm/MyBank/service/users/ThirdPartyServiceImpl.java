package com.midterm.MyBank.service.users;

import com.midterm.MyBank.model.Users.ThirdParty;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.users.utils.AccountActions;
import com.midterm.MyBank.service.users.utils.PasswordUtil;
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


    public void deposit(String username, String hashedKey, Long recipientAccountId, String secretKey, Money money){
        //username already exists since route cannot be accessed otherwise
        ThirdParty thirdPartyUser = (ThirdParty) userRepository.findByUsername(username).get();
        if (Objects.equals(hashedKey, PasswordUtil.encryptPassword(thirdPartyUser.getSecurityKey()))){
            //hashed key from http is the same as the password encrypted
            if (!(accountActions.find(recipientAccountId) == null)){
                switch(accountActions.find(recipientAccountId).getClass().getSimpleName()){
                    case "Checking":
                       Checking checkingAccount = (Checking) accountActions.find(recipientAccountId);
                       if (Objects.equals(checkingAccount.getSecretKey(), secretKey)){
                           checkingAccount.setBalance(new Money(checkingAccount.getBalance().increaseAmount(money)));
                       }
                        break;
                    case "StudentChecking":
                        StudentChecking studentCheckingAccount = (StudentChecking) accountActions.find(recipientAccountId);
                        if (Objects.equals(studentCheckingAccount.getSecretKey(), secretKey)){
                            studentCheckingAccount.setBalance(new Money(studentCheckingAccount.getBalance().increaseAmount(money)));
                        }
                        break;
                    case "Savings":
                        Savings savingsAccount = (Savings) accountActions.find(recipientAccountId);
                        if (Objects.equals(savingsAccount.getSecretKey(), secretKey)){
                            savingsAccount.setBalance(new Money(savingsAccount.getBalance().increaseAmount(money)));
                        }
                        break;
                    case "CreditCard":
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Third Parties cannot transfer to credit accounts");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id is incorrect");
            }
        } else{
            //incorrect hashed key
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid key from third party");
        }
    }

    public void withdraw(String username, String hashedKey, Long recipientAccountId, String secretKey, Money money){
        //username already exists since route cannot be accessed otherwise
        ThirdParty thirdPartyUser = (ThirdParty) userRepository.findByUsername(username).get();
        if (Objects.equals(hashedKey, PasswordUtil.encryptPassword(thirdPartyUser.getSecurityKey()))){
            //hashed key from http is the same as the password encrypted
            if (!(accountActions.find(recipientAccountId) == null)){
                switch(accountActions.find(recipientAccountId).getClass().getSimpleName()){
                    case "Checking":
                        Checking checkingAccount = (Checking) accountActions.find(recipientAccountId);
                        if (Objects.equals(checkingAccount.getSecretKey(), secretKey)){
                            //account secret key matches
                            if (accountActions.enoughFunds(checkingAccount.getId(), money)){
                                //There are enough funds to perform the withdrawal
                                checkingAccount.setBalance(new Money(checkingAccount.getBalance().decreaseAmount(money)));
                            } else {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds");
                            }
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account secret key does not match");
                        }
                        break;
                    case "StudentChecking":
                        StudentChecking studentCheckingAccount = (StudentChecking) accountActions.find(recipientAccountId);
                        if (Objects.equals(studentCheckingAccount.getSecretKey(), secretKey)){
                            //account secret key matches
                            if (accountActions.enoughFunds(studentCheckingAccount.getId(), money)){
                                //There are enough funds to perform the withdrawal
                                studentCheckingAccount.setBalance(new Money(studentCheckingAccount.getBalance().decreaseAmount(money)));
                            } else {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds");
                            }
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account secret key does not match");
                        }
                        break;
                    case "Savings":
                        Savings savingsAccount = (Savings) accountActions.find(recipientAccountId);
                        if (Objects.equals(savingsAccount.getSecretKey(), secretKey)){
                            //account secret key matches
                            if (accountActions.enoughFunds(savingsAccount.getId(), money)){
                                //There are enough funds to perform the withdrawal
                                savingsAccount.setBalance(new Money(savingsAccount.getBalance().decreaseAmount(money)));
                            } else {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds");
                            }
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account secret key does not match");
                        }
                        break;
                    case "CreditCard":
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Third Parties cannot transfer to credit accounts");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id is incorrect");
            }
        } else{
            //incorrect hashed key
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid key from third party");
        }
    }
}
