package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.service.accounts.srv.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingAccountService;

    @GetMapping("/accounts/checking/{id}")
    public Checking get(@PathVariable long id){
        return checkingAccountService.get(id);
    }

    @PostMapping("/accounts/checking")
    public Account create(@RequestBody Checking checkingAccount){
        return checkingAccountService.save(checkingAccount);
    }
    @PutMapping("/accounts/checking/{id}")
    public Checking update(@PathVariable long id, @RequestBody Checking checkingAccount){
        return checkingAccountService.update(checkingAccount, id);
    }

    @PatchMapping("/accounts/checking/{id}/transfer")
    public Checking transfer(@PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        return checkingAccountService.transfer(id, recipientId, money);
    }


    @DeleteMapping("/accounts/checking/{id}")
    public void delete (@PathVariable long id, @RequestBody Checking checkingAccount){
        checkingAccountService.delete(checkingAccount);
    }


}
