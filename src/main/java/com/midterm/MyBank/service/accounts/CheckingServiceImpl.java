package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.controller.dto.CheckingDTO;
import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Users.Admin;
import com.midterm.MyBank.model.Users.ThirdParty;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.model.security.User;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.RoleRepository;
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
import static org.apache.tomcat.jni.SSL.setPassword;

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
    @Autowired
    RoleRepository roleRepository;

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
    public CheckingDTO test(String hello) {
        CheckingDTO checkingDTO = new CheckingDTO();
        checkingDTO.setPrimaryOwnerId(1L);
        checkingDTO.setBalance(new Money(new BigDecimal("888")));
        checkingDTO.setSecretKey("123");
        return checkingDTO;
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
    public void UsersAndAccountCreationForTesting(){
        //CREATING USERS
        LocalDate dateOfBirth = LocalDate.now();
        LocalDate date1992 = LocalDate.of(1992, 1, 8);
        LocalDate date2000 = LocalDate.of(2000, 12, 4);

        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120" );
        AccountHolder userJose = new AccountHolder("jose", encryptPassword("jose123"), "Jose garcía", date1992, address);
        AccountHolder userPepita = new AccountHolder("pepita", encryptPassword("pepita123"), "Pepa sánchez", date2000, address);
        Admin adminSergio = new Admin("sergio", encryptPassword("sergio2022"), "sergio doste");
        ThirdParty movistar = new ThirdParty("movistar", encryptPassword("movistar2022"), "movistar", encryptPassword("123"));

        //ASSIGNING ROLES
        Role admin = new Role("ADMIN");
        Set<User> users = new HashSet<>();
        users.add(adminSergio);
        admin.setUsers(users);
        Set<Role> roles1 = new HashSet<>();
        roles1.add(admin);
        adminSergio.setRoles(roles1);

        Role accountholder = new Role("ACCOUNTHOLDER");
        Set<User> users2 = new HashSet<>();
        users2.add(userJose);
        users2.add(userPepita);
        accountholder.setUsers(users2);
        Set<Role> roles2 = new HashSet<>();
        roles2.add(accountholder);
        userJose.setRoles(roles2);
        userPepita.setRoles(roles2);

        Role thirdparty = new Role("THIRDPARTY");
        Set<User> users3 = new HashSet<>();
        users3.add(movistar);
        thirdparty.setUsers(users3);
        Set<Role> roles3 = new HashSet<>();
        roles3.add(thirdparty);
        movistar.setRoles(roles3);
        //SAVING THEM
        roleRepository.save(admin);
        roleRepository.save(accountholder);
        roleRepository.save(thirdparty);
        userRepository.save(userJose);
        userRepository.save(userPepita);
        userRepository.save(adminSergio);
        userRepository.save(movistar);

        //ACCOUNT CREATION

        //CHECKING

        //SAVINGS

        //CREDIT CARD
        AccountHolder recoveredUserJose = (AccountHolder) userRepository.findById(userJose.getId()).get();
//        Admin recoveredAdminSergio = (Admin) userRepository.findById(2L).get();

        Checking pepeCheckingAccount = new Checking("123", recoveredUserJose, new Money(new BigDecimal("1111")));
        Checking MariaCheckingAccount = new Checking("123", userPepita, new Money(new BigDecimal("2222")));
        checkingRepository.save(pepeCheckingAccount);
        checkingRepository.save(MariaCheckingAccount);
    }
}
