package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.accounts.interfaces.StudentCheckingService;
import com.midterm.MyBank.service.users.utils.AccountActions;
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
        if (studentCheckingRepository.findById(id).isPresent()){
            //account exists
            return studentCheckingRepository.findById(id).get();
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no Student Checking Account with this id");
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
        //checking if issuing account exists
        if (studentCheckingRepository.findById(userId).isPresent()){
            //checking if account is a Credit Card
            if (accountActions.find(userId).getClass().getSimpleName() == "StudentChecking"){
                //accountActions checks if recipientId is valid, if enough funds in user account, and does the transfer
                return (StudentChecking) accountActions.transfer(userId, recipientId, money);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be from a student checking account");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Checking Account Id is invalid");
        }
    }

    @Override
    public void delete(StudentChecking studentCheckingAccount) {
        try {
            studentCheckingRepository.delete(studentCheckingAccount);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting account");
        }
    }
}
