package com.midterm.MyBank.service.utils;

import com.midterm.MyBank.controller.dto.AccountDTO;

import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.repository.SavingsRepository;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class YourHomePageServiceImpl implements YourHomePageService {
    @Autowired
    AccountActions accountActions;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Set<AccountDTO> get(String username){
        long accountHolderId = userRepository.findByUsername(username).get().getId();
        Set<AccountDTO> accountDTOsList = new HashSet<>();
        //Find all Checking accounts
        List<Checking> checkingList = checkingRepository.findAllByPrimaryOwnerId(accountHolderId);
        for (Checking checkingAccount : checkingList){
            AccountDTO accountDTO = new AccountDTO(checkingAccount.getId(), "Checking", checkingAccount.getBalance());
            accountDTOsList.add(accountDTO);
        }
        //Find all Student Checking accounts
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAllByPrimaryOwnerId(accountHolderId);
        for (StudentChecking studentChecking : studentCheckingList){
            AccountDTO accountDTO = new AccountDTO(studentChecking.getId(), "StudentChecking", studentChecking.getBalance());
            accountDTOsList.add(accountDTO);
        }
        //Find all Saving accounts
        List<Savings> savingsList = savingsRepository.findAllByPrimaryOwnerId(accountHolderId);
        for (Savings savingsAccount : savingsList){
            AccountDTO accountDTO = new AccountDTO(savingsAccount.getId(), "Savings", savingsAccount.getBalance());
            accountDTOsList.add(accountDTO);
        }
        //Find all Credit Card accounts
        List<CreditCard> creditCardList = creditCardRepository.findAllByPrimaryOwnerId(accountHolderId);
        for (CreditCard creditCardAccount : creditCardList){
            AccountDTO accountDTO = new AccountDTO(creditCardAccount.getId(), "CreditCard", creditCardAccount.getBalance());
            accountDTOsList.add(accountDTO);
        }

        return accountDTOsList;
    }
}
