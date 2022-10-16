package com.midterm.MyBank.config.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import com.midterm.MyBank.service.accounts.interfaces.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StudentCheckingController {
    @Autowired
    StudentCheckingService studentCheckingService;

    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @GetMapping("/accounts/{username}/checking/student/{id}")
    public StudentChecking get(@PathVariable String username, @PathVariable long id){
        return studentCheckingService.get(username, id);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/accounts/{username}/checking/student/{id}")
    public StudentChecking update(@PathVariable long id, @RequestBody StudentChecking studentCheckingAccount){
        return studentCheckingService.update(studentCheckingAccount, id);
    }
    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @PatchMapping("/accounts/{username}checking/student/{id}/transfer")
    public StudentChecking transfer(@PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        return studentCheckingService.transfer(id, recipientId, money);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/accounts/{username}/checking/student/{id}")
    public void delete (@PathVariable long id, @RequestBody StudentChecking studentCheckingAccount){
        studentCheckingService.delete(studentCheckingAccount);
    }

}