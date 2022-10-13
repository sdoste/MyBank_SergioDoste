package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.service.accounts.interfaces.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class SavingsController {
    @Autowired
    SavingsService savingsService;

    @GetMapping("/accounts/savings/{id}")
    public Savings access(@PathVariable long id){
        savingsService.yearlyInterestApplied(id);
        return savingsService.get(id);
    }
    @PostMapping("/accounts/savings")
    public Savings create(@RequestBody Savings savings){
        return savingsService.save(savings);
    }
    @PutMapping("/accounts/savings/{id}")
    public Savings update(@PathVariable long id, @RequestBody Savings savings){
        return savingsService.update(savings, id);
    }

    @DeleteMapping("accounts/savings/{id}")
    public void delete (@PathVariable long id, @RequestBody Savings savings){
        savingsService.delete(savings);
    }
}
