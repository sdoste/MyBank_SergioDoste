package com.midterm.MyBank.service.utils;

import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Users.Admin;
import com.midterm.MyBank.model.Users.ThirdParty;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.*;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.RoleRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;

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
    @Autowired
    RoleRepository roleRepository;

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
        if (money.getAmount().compareTo(recoveredAccount.getBalance().getAmount()) > 0){
            return false;
        } else{
            return true;
        }
    }

    @PostConstruct
    public void UsersAndAccountCreationForTesting(){
        //CREATING USERS
        LocalDate dateOfBirth = LocalDate.now();
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        LocalDate date2000 = LocalDate.of(2000, 12, 4);

        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120" );
        AccountHolder userJose = new AccountHolder("jose", encryptPassword("jose123"), "Jose garcía", date1992, address);
        AccountHolder userPepita = new AccountHolder("pepita", encryptPassword("pepita123"), "Pepa sánchez", date2000, address);
        Admin adminSergio = new Admin("sergio", encryptPassword("sergio2022"), "sergio doste");
        ThirdParty movistar = new ThirdParty("movistar", encryptPassword("movistar2022"), "movistar", "123");

        //ASSIGNING ROLES
        Role admin = new Role("ADMIN");
        admin.setId(1L);
        roleRepository.save(admin);
        adminSergio.setRoles(Set.of(admin));
//

        Role accountholder = new Role("ACCOUNTHOLDER");
        accountholder.setId(2L);
        roleRepository.save(accountholder);
        userJose.setRoles(Set.of(accountholder));
        userPepita.setRoles(Set.of(accountholder));

//
        Role thirdparty = new Role("THIRDPARTY");
        thirdparty.setId(3L);
        roleRepository.save(thirdparty);
        movistar.setRoles(Set.of(thirdparty));

        userRepository.save(userJose);
        userRepository.save(userPepita);
        userRepository.save(adminSergio);
        userRepository.save(movistar);

        //ACCOUNT CREATION

        //CHECKING

        //SAVINGS
        Savings pepitaSavingsAccount = new Savings("000", userPepita, new BigDecimal("0.34"),
                new BigDecimal("500"), new Money(new BigDecimal("1500")));
        //CREDIT CARD
        AccountHolder recoveredUserJose = (AccountHolder) userRepository.findById(userJose.getId()).get();
//        Admin recoveredAdminSergio = (Admin) userRepository.findById(2L).get();

        Checking joseCheckingAccount = new Checking("123", recoveredUserJose, new Money(new BigDecimal("1111")));
        CreditCard joseCreditCardAccount = new CreditCard(new Money(new BigDecimal("1500")), userJose, new BigDecimal("10000"), new BigDecimal("0.020"));

        checkingRepository.save(joseCheckingAccount);
        creditCardRepository.save(joseCreditCardAccount);
        savingsRepository.save(pepitaSavingsAccount);
    }
}
