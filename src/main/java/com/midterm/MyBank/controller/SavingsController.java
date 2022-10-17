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
    @PatchMapping("/accounts/{username}savings/{id}/transfer")
    public Savings transfer(@PathVariable String username, @PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
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
    @PatchMapping("/accounts/savings/{id}")
    public Savings update(@PathVariable long id, @RequestBody Savings savingsAccount){
        return savingsService.update(savingsAccount, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/savings/{id}")
    public void delete (@PathVariable long id, @RequestBody Savings savingsAccount){
        savingsService.delete(savingsAccount);
    }
}
