package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class CreditCardController {
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    CreditCardRepository creditCardRepository;

    //USER ACTIONS
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/savings/{id}")
    public CreditCard get(@PathVariable String username, @PathVariable long id){
        if (creditCardRepository.findById(id).isPresent() &&
                (Objects.equals(creditCardRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return creditCardService.get(username, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}savings/{id}/transfer")
    public CreditCard transfer(@PathVariable String username, @PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        if (creditCardRepository.findById(id).isPresent() &&
                (Objects.equals(creditCardRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return creditCardService.transfer(id, recipientId, money);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    //ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/savings")
    public CreditCard create(@RequestBody CreditCardDTO savingsAccountDTO){
        return creditCardService.save(savingsAccountDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/savings/{id}")
    public CreditCard update(@PathVariable long id, @RequestBody CreditCard savingsAccount){
        return creditCardService.update(savingsAccount, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/savings/{id}")
    public void delete (@PathVariable long id, @RequestBody CreditCard savingsAccount){
        creditCardService.delete(savingsAccount);
    }
}
