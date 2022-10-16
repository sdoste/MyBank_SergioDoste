package com.midterm.MyBank.config.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import com.midterm.MyBank.service.accounts.interfaces.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
public class SavingsController {
    @Autowired
    SavingsService savingsService;

    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @GetMapping("/accounts/{username}/savings/{id}")
    public Savings get(@PathVariable String username, @PathVariable long id){
        return savingsService.get(username, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @PostMapping("/accounts/{username}/savings")
    public Savings create(@RequestBody Savings savingsAccount){
        return savingsService.save(savingsAccount);
    }
    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @PutMapping("/accounts/{username}/savings/{id}")
    public Savings update(@PathVariable long id, @RequestBody Savings savingsAccount){
        return savingsService.update(savingsAccount, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @PatchMapping("/accounts/{username}savings/{id}/transfer")
    public Savings transfer(@PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        return savingsService.transfer(id, recipientId, money);
    }

    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @DeleteMapping("/accounts/{username}/savings/{id}")
    public void delete (@PathVariable long id, @RequestBody Savings savingsAccount){
        savingsService.delete(savingsAccount);
    }
}
