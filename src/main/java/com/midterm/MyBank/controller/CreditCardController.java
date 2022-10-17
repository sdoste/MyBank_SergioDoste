package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CreditCardController {
    @Autowired
    CreditCardService creditCardService;

    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/creditCard/{id}")
    public CreditCard get(@PathVariable String username, @PathVariable long id){
        return creditCardService.get(username, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PostMapping("/accounts/{username}/creditCard")
    public CreditCard create(@RequestBody CreditCard creditCardAccount){
        return creditCardService.save(creditCardAccount);
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}/creditCard/{id}")
    public CreditCard update(@PathVariable long id, @RequestBody CreditCard creditCardAccount){
        return creditCardService.update(creditCardAccount, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}creditCard/{id}/transfer")
    public CreditCard transfer(@PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        return creditCardService.transfer(id, recipientId, money);
    }

    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @DeleteMapping("/accounts/{username}/creditCard/{id}")
    public void delete (@PathVariable long id, @RequestBody CreditCard creditCardAccount){
        creditCardService.delete(creditCardAccount);
    }

}
