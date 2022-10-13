package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class CreditCardController {
    @Autowired
    CreditCardService creditCardService;

    @GetMapping("/accounts/creditCards/{id}")
    public CreditCard access(@PathVariable long id){
        creditCardService.monthlyInterestApplied(id);
        return creditCardService.get(id);
    }
    @PostMapping("/accounts/creditCards")
    public CreditCard create(@RequestBody CreditCard creditCard){
        return creditCardService.save(creditCard);
    }
    @PutMapping("/accounts/creditCards/{id}")
    public CreditCard update(@PathVariable long id, @RequestBody CreditCard creditCard){
        return creditCardService.update(creditCard, id);
    }

    @DeleteMapping("/accounts/creditCards/{id}")
    public void delete (@PathVariable long id, @RequestBody CreditCard creditCard){
        creditCardService.delete(creditCard);
    }
}
