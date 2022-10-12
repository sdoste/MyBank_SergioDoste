package com.midterm.MyBank.service.accounts.impl;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Users.Admin;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.srv.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Period;

@Service
public class CheckingServiceImpl implements CheckingService {
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Checking get(long id) {
        return checkingRepository.findById(id).get();
    }

    @Override
    public Account save(Checking checkingAccount) {
    //if age is greater than 24, create checking account
//        if ((LocalDate.now().getYear() - checkingAccount.getPrimaryOwner().getDateOfBirth().getYear()) > 24){
        if (Period.between(checkingAccount.getPrimaryOwner().getDateOfBirth(), LocalDate.now()).getYears() > 24){
            return checkingRepository.save(checkingAccount);
        } else {
            //if age less than 24, we create and save a checking account
            StudentChecking studentChecking = new StudentChecking();
            studentChecking.setSecretKey(checkingAccount.getSecretKey());
            studentChecking.setPrimaryOwner(checkingAccount.getPrimaryOwner());
            if (checkingAccount.getSecondaryOwner() == null){
                studentChecking.setSecondaryOwner(null);
            }else {
                studentChecking.setSecondaryOwner(checkingAccount.getSecondaryOwner());

            }
            return studentCheckingRepository.save(studentChecking);
        }
    }

    @Override
    public Checking update(Checking checkingAccount, long id) {
        Checking recoveredObject = checkingRepository.findById(id).get();
        recoveredObject.setBalance(checkingAccount.getBalance());
        return checkingRepository.save(recoveredObject);
    }

    @Override
    public Account getAccount(long id){
        if (checkingRepository.findById(id).isPresent()){
            return checkingRepository.findById(id).get();
        } else if (studentCheckingRepository.findById(id).isPresent()) {
            return studentCheckingRepository.findById(id).get();
        } else if (creditCardRepository.findById(id).isPresent()){
            return creditCardRepository.findById(id).get();

        } else {
            System.out.println("Account holder id is invalid");
            return null;
        }
    }

    @Override
    public boolean enoughFunds(long userId, Money money){
        Account recoveredAccount = checkingRepository.findById(userId).get();
        if (recoveredAccount.getBalance().getAmount().compareTo(money.getAmount()) > 0){
            return false;
        } else{
            return true;
        }
    }
    @Override
    public Checking transfer(long userId, long recipientId, Money money){
        if (checkingRepository.findById(userId).isPresent()){
            //User id VALID
            Checking recoveredAccount = checkingRepository.findById(userId).get();
            if (getAccount(recipientId) == null){
                //Recipient Id INVALID
                System.out.println("Wrong recipient Id");
            } else{
                //Recipient Id VALID
                Account recipientAccount = getAccount(recipientId);
                if (enoughFunds(userId, money)) {
                    //ENOUGH FUNDS
                    recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(money)));
                    recipientAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(money)));
                    return recoveredAccount;
                }  else {
                    //NOT ENOUGH FUNDS
                    System.out.println("Insufficient funds in your account");
                }
            }
        }else {
            //USER ID INVALID
            System.out.println("User id is invalid");
        }
        return null;
    }


    @Override
    public void delete(Checking checkingAccount) {
        checkingRepository.delete(checkingAccount);
    }

    @PostConstruct
    public void AccountHolderAndAdminCreation(){
//        String name = "pepe";
//        LocalDate dateOfBirth = LocalDate.now();
//        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120" );
//        AccountHolder userPepe = new AccountHolder(name, dateOfBirth, address);
//        Admin adminSergio = new Admin("sergio");
//        //usernames and passwords
//        userPepe.setUsername("pepe");
//        userPepe.setPassword("pepe123");
//
//        adminSergio.setUsername("sergio");
//        adminSergio.setPassword("sergio2022");
//
//        userRepository.save(userPepe);
//        userRepository.save(adminSergio);
//
//        AccountHolder recoveredUserPepe = (AccountHolder) userRepository.findById(1L).get();
//        Admin recoveredAdminSergio = (Admin) userRepository.findById(2L).get();
//
//        Checking pepeChecking = new Checking("123", recoveredUserPepe);
//        checkingRepository.save(pepeChecking);
    }
}
