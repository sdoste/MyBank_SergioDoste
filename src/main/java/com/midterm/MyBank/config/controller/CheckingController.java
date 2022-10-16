package com.midterm.MyBank.config.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingService;

    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @GetMapping("/accounts/{username}/checking/{id}")
    public Checking get(@PathVariable String username, @PathVariable long id){
        return checkingService.get(username, id);
    }
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/accounts/{username}/checking")
    public Account create(@RequestBody Checking checkingAccount){
        return checkingService.save(checkingAccount);
    }
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/accounts/{username}/checking/{id}")
    public Checking update(@PathVariable long id, @RequestBody Checking checkingAccount){
        return checkingService.update(checkingAccount, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @PatchMapping("/accounts/{username}checking/{id}/transfer")
    public Checking transfer(@PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        return checkingService.transfer(id, recipientId, money);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/accounts/{username}/checking/{id}")
    public void delete (@PathVariable long id, @RequestBody Checking checkingAccount){
        checkingService.delete(checkingAccount);
    }


}
