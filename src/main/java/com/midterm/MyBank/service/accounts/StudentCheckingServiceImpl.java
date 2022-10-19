package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.interfaces.StudentCheckingService;
import com.midterm.MyBank.service.utils.AccountActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentCheckingServiceImpl implements StudentCheckingService {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    //component with methods to transfer, find accounts (any type) and check if enough funds
    @Autowired
    AccountActions accountActions;
    @Autowired
    UserRepository userRepository;

    @Override
    public StudentChecking get(String username, long id) {
        return studentCheckingRepository.findById(id).get();
    }
    @Override
    public StudentChecking modifyBalance(Money newBalance, long id){
        if (studentCheckingRepository.findById(id).isPresent()){
            //account exists
            StudentChecking recoveredAccount = studentCheckingRepository.findById(id).get();
            recoveredAccount.setBalance(newBalance);
            return studentCheckingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
    @Override
    public StudentChecking increaseBalance(Money addedBalance, long id){
        if (studentCheckingRepository.findById(id).isPresent()){
            //account exists
            StudentChecking recoveredAccount = studentCheckingRepository.findById(id).get();
            recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().increaseAmount(addedBalance)));
            return studentCheckingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
    @Override
    public StudentChecking decreaseBalance(Money subtractedBalance, long id){
        if (studentCheckingRepository.findById(id).isPresent()){
            //account exists
            StudentChecking recoveredAccount = studentCheckingRepository.findById(id).get();
            recoveredAccount.setBalance(new Money(recoveredAccount.getBalance().decreaseAmount(subtractedBalance)));
            return studentCheckingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
    @Override
    public StudentChecking update(StudentChecking studentCheckingAccount, long id) {
        if (studentCheckingRepository.findById(id).isPresent()){
            //account exists
            StudentChecking recoveredAccount = studentCheckingRepository.findById(id).get();
            recoveredAccount.setBalance(studentCheckingAccount.getBalance());
            return studentCheckingRepository.save(recoveredAccount);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }


    @Override
    public StudentChecking transfer(long userId, long recipientId, Money money){
        StudentChecking studentCheckingAccount = studentCheckingRepository.findById(userId).get();
        //AA checks if recipientId valid, if enough funds in account, and adds money
        if (accountActions.transferred(userId, recipientId, money)){
            //so money has been added to another account, so it's subtracted from this account
            studentCheckingAccount.setBalance(new Money(studentCheckingAccount.getBalance().decreaseAmount(money)));
            studentCheckingRepository.save(studentCheckingAccount);
            return studentCheckingAccount;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while transferring money");
        }
    }

    @Override
    public void delete(long id) {
        try {
            studentCheckingRepository.deleteById(id);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting account");
        }
    }
}
