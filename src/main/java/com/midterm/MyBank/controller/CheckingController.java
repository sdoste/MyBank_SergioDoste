package com.midterm.MyBank.controller;

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
    CheckingService checkingAccountService;

    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @GetMapping("/accounts/{username}/checking/{id}")
    public Checking get(@PathVariable String username, @PathVariable long id){
          try {
            return checkingAccountService.get(username, id);
        }
            catch(Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access denied");
            }
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
