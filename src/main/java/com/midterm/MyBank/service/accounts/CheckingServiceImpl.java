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
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;

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
    public Checking get(String username, long id) {
        if (userRepository.findByUsername(username).isPresent()){
            //user exists
            User user = userRepository.findByUsername(username).get();
            if (!(getAccount(id) == null)){
                //account exists
                return checkingRepository.findById(id).get();
            } else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
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
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120" );
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
//        Checking pepeChecking = new Checking("123", recoveredUserPepe);
//        checkingRepository.save(pepeChecking);

//        AccountHolder userMaria = new AccountHolder("Maria", LocalDate.now(), address);
//        userMaria.setUsername("maria");
//        userMaria.setPassword(encryptPassword("maria123"));
//        userRepository.save(userMaria);
    }
}
