package com.midterm.MyBank.controller;

import com.midterm.MyBank.controller.dto.CheckingDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingService;

    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/checking/{id}")
    public Checking get(@PathVariable String username, @PathVariable long id){
        return checkingService.get(username, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/checking")
    public Account create(@RequestBody CheckingDTO checkingDTO){
        return checkingService.save(checkingDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/checking/test")
    public CheckingDTO test(@RequestBody String hello){
        return checkingService.test(hello);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}/checking/{id}")
    public Checking update(@PathVariable long id, @RequestBody Checking checkingAccount){
        return checkingService.update(checkingAccount, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}checking/{id}/transfer")
    public Checking transfer(@PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        return checkingService.transfer(id, recipientId, money);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/checking/{id}")
    public void delete (@PathVariable long id, @RequestBody Checking checkingAccount){
        checkingService.delete(checkingAccount);
    }


}
