package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.controller.dto.CheckingDTO;
import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Users.Admin;
import com.midterm.MyBank.model.Users.ThirdParty;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.RoleRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import com.midterm.MyBank.service.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;

@Service
public class CheckingServiceImpl implements CheckingService {
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    //component with methods to transfer, find accounts (any type) and check if enough funds
    @Autowired
    AccountActions accountActions;
    @Autowired
    UserRepository userRepository;



    @Override
    public Checking get(String username, long id) {
         return checkingRepository.findById(id).get();
    }

    @Override
    public Account save(CheckingDTO checkingDTO) {
        try {
            if (userRepository.findById(checkingDTO.getPrimaryOwnerId()).isPresent()) {
                AccountHolder primaryOwner = (AccountHolder) userRepository.findById(checkingDTO.getPrimaryOwnerId()).get();
                //if age is greater than 24, create checking account
                if (Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears() > 24) {
                    Checking newAccount = new Checking(checkingDTO.getSecretKey(), primaryOwner, checkingDTO.getBalance());
                    return checkingRepository.save(newAccount);
                } else {
                    //if age less than 24, we create and save a checking account
                    StudentChecking newAccount = new StudentChecking(checkingDTO.getSecretKey(), primaryOwner, checkingDTO.getBalance());
                    return studentCheckingRepository.save(newAccount);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect primary Owner Id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving the Checking account");
        }
    }
    @Override
    public Checking modifyBalance(Money newBalance, long id){
        if (checkingRepository.findById(id).isPresent()){
            //account exists
            Checking recoveredAccount = checkingRepository.findById(id).get();
            recoveredAccount.setBalance(newBalance);
            return checkingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
    @Override
    public Checking increaseBalance(Money addedBalance, long id){
        if (checkingRepository.findById(id).isPresent()){
            //account exists
            Checking recoveredAccount = checkingRepository.findById(id).get();
            recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(addedBalance)));
            return checkingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
    @Override
    public Checking decreaseBalance(Money subtractedBalance, long id){
        if (checkingRepository.findById(id).isPresent()){
            //account exists
            Checking recoveredAccount = checkingRepository.findById(id).get();
            recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(subtractedBalance)));
            return checkingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
    @Override
    public Checking update(Checking checkingAccount, long id) {
        if (checkingRepository.findById(id).isPresent()){
            //account exists
            Checking recoveredAccount = checkingRepository.findById(id).get();
            recoveredAccount.setBalance(checkingAccount.getBalance());
            return checkingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }

    @Override
    public Checking transfer(long userId, long recipientId, Money money){
        //checking if account is a Credit Card
        if (accountActions.find(userId).getClass().getSimpleName().equals("Checking")){
            //accountActions checks if recipientId is valid, if enough funds in user account, and does the transfer
            return (Checking) accountActions.transfer(userId, recipientId, money);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be from a checking account");
        }
    }

    @Override
    public void delete(Checking checkingAccount) {
        try {
            checkingRepository.delete(checkingAccount);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting account");
        }
    }
}
