package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Users.Admin;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.model.security.User;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import com.midterm.MyBank.service.users.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import static com.midterm.MyBank.service.users.utils.PasswordUtil.encryptPassword;

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
        if (checkingRepository.findById(id).isPresent()){
            //account exists
         return checkingRepository.findById(id).get();
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no Checking Account with this id");
        }
    }

    @Override
    public Account save(Checking checkingAccount) {
        try{
            //if age is greater than 24, create checking account
            if (Period.between(checkingAccount.getPrimaryOwner().getDateOfBirth(), LocalDate.now()).getYears() > 24){
                return checkingRepository.save(checkingAccount);
            } else {
                //if age less than 24, we create and save a checking account
                StudentChecking studentChecking = new StudentChecking();
                studentChecking.setSecretKey(checkingAccount.getSecretKey());
                studentChecking.setPrimaryOwner(checkingAccount.getPrimaryOwner());
                if (!(checkingAccount.getSecondaryOwner() == null)){
                    studentChecking.setSecondaryOwner(checkingAccount.getSecondaryOwner());
                }
                return studentCheckingRepository.save(studentChecking);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving the Checking account");
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
        //checking if issuing account exists
        if (checkingRepository.findById(userId).isPresent()){
            //checking if account is a Credit Card
            if (accountActions.find(userId).getClass().getSimpleName() == "Checking"){
                //accountActions checks if recipientId is valid, if enough funds in user account, and does the transfer
                return (Checking) accountActions.transfer(userId, recipientId, money);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be from a checking account");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checking Account Id is invalid");
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

    @PostConstruct
    public void AccountHolderAndAdminCreation(){
//        String name = "pepe";
//        LocalDate dateOfBirth = LocalDate.now();
//        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120" );
//        AccountHolder userPepe = new AccountHolder(name, dateOfBirth, address);
//        Admin adminSergio = new Admin("sergio");
//        //usernames and passwords
//        userPepe.setUsername("pepe");
//        userPepe.setPassword(encryptPassword("pepe123"));
//
//        adminSergio.setUsername("sergio");
//        adminSergio.setPassword(encryptPassword("sergio2022"));
//
//        Role admin = new Role();
//        admin.setName("admin");
//        Set<User> users = new HashSet<>();
//        users.add(adminSergio);
//        admin.setUsers(users);
//
//        Role accountholder = new Role();
//        accountholder.setName("accountholder");
//        Set<User> users2 = new HashSet<>();
//        users2.add(userPepe);
//        admin.setUsers(users2);
//
//        userRepository.save(userPepe);
//        userRepository.save(adminSergio);
//
//        AccountHolder recoveredUserPepe = (AccountHolder) userRepository.findById(1L).get();
//        Admin recoveredAdminSergio = (Admin) userRepository.findById(2L).get();
//
//        Checking pepeChecking = new Checking("123", recoveredUserPepe, new Money(new BigDecimal("555")));
//        checkingRepository.save(pepeChecking);
//
//        AccountHolder userMaria = new AccountHolder("Maria", LocalDate.now(), address);
//        userMaria.setUsername("maria");
//        userMaria.setPassword(encryptPassword("maria123"));
//        userRepository.save(userMaria);
    }
}
