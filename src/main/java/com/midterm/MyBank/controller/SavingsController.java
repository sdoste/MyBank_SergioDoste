package com.midterm.MyBank.controller;

import com.midterm.MyBank.controller.dto.SavingsDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.service.accounts.interfaces.SavingsService;
import com.midterm.MyBank.repository.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestController
public class SavingsController {
    @Autowired
    SavingsService savingsService;
    @Autowired
    SavingsRepository savingsRepository;

    //USER ACTIONS
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/savings/{id}")
    public Savings get(@PathVariable String username, @PathVariable long id){
        if (savingsRepository.findById(id).isPresent() &&
                (Objects.equals(savingsRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return savingsService.get(username, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}/savings/{id}/transferTo/{recipientId}")
    public Savings transfer(@PathVariable String username, @PathVariable long id, @PathVariable long recipientId, @RequestBody Money money){
        if (savingsRepository.findById(id).isPresent() &&
                (Objects.equals(savingsRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return savingsService.transfer(id, recipientId, money);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    //ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/savings")
    public Savings create(@RequestBody SavingsDTO savingsAccountDTO){
        return savingsService.save(savingsAccountDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/accounts/savings/{id}/interestDate/{newDate}")
    public Savings updateLastAppliedInterestRate(@PathVariable long id, @PathVariable String newDate){
        if (savingsRepository.findById(id).isPresent()){
            //account exists
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(newDate, formatter);
            } catch(Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date format");
            }
            return savingsService.updateLastAppliedInterestRate(localDate, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saving account does not exist");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/savings/{id}/modifyBalance")
    public Savings modifyBalance(@PathVariable long id, @RequestBody Money newBalance){
        return savingsService.modifyBalance(newBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/savings/{id}/increaseBalance")
    public Savings increaseBalance(@PathVariable long id, @RequestBody Money addedBalance){
        return savingsService.increaseBalance(addedBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/savings/{id}/decreaseBalance")
    public Savings decreaseBalance(@PathVariable long id, @RequestBody Money subtractedBalance){
        return savingsService.decreaseBalance(subtractedBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/savings/{id}")
    public void delete (@PathVariable long id){
        if (savingsRepository.findById(id).isPresent()){
            savingsService.delete(id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect account id");
        }
    }

}
