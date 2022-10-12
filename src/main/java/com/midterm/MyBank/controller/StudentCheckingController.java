package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.accounts.Savings;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.service.accounts.srv.SavingsService;
import com.midterm.MyBank.service.accounts.srv.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentCheckingController {
    @Autowired
    StudentCheckingService studentCheckingService;

    @GetMapping("/accounts/studentChecking/{id}")
    public StudentChecking get(@PathVariable long id){
        return studentCheckingService.get(id);
    }
    //student accounts should be created from CheckingAccount,(age should be less than 24)
//    @PostMapping("/studentAccounts")
//    public Savings create(@RequestBody Savings savings){
//        return savingsService.save(savings);
//    }
    @PutMapping("/accounts/studentChecking/{id}")
    public StudentChecking update(@PathVariable long id, @RequestBody StudentChecking studentChecking){
        return studentCheckingService.update(studentChecking, id);
    }

    @DeleteMapping("/accounts/studentChecking/{id}")
    public void delete (@PathVariable long id, @RequestBody StudentChecking studentChecking){
        studentCheckingService.delete(studentChecking);
    }
}