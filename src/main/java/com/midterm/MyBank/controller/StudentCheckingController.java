package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.service.accounts.interfaces.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class StudentCheckingController {
    @Autowired
    StudentCheckingService studentCheckingService;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    //ACCOUNTHOLDER
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/checking/student/{id}")
    public StudentChecking get(@PathVariable String username, @Validated @PathVariable long id){
        if (studentCheckingRepository.findById(id).isPresent() &&
                (Objects.equals(studentCheckingRepository.findById(id).get().getPrimaryOwner().getUsername(), username))){
            return studentCheckingService.get(username, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}/checking/student/{id}/transferTo/{recipientId}")
    public StudentChecking transfer(@PathVariable String username, @PathVariable long id, @PathVariable long recipientId, @RequestBody Money money){
        //checking Account id exists, primaryOwner is the logged in user, and is a student checking account
        if (studentCheckingRepository.findById(id).isPresent() &&
                (Objects.equals(studentCheckingRepository.findById(id).get().getPrimaryOwner().getUsername(), username))){
            return studentCheckingService.transfer(id, recipientId, money);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Checking Account id not found for this username");
        }
    }

    //ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/student/{id}/modifyBalance")
    public StudentChecking modifyBalance(@PathVariable long id, @RequestBody Money newBalance){
        return studentCheckingService.modifyBalance(newBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/student/{id}/increaseBalance")
    public StudentChecking increaseBalance(@PathVariable long id, @RequestBody Money addedBalance){
        return studentCheckingService.increaseBalance(addedBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/student/{id}/decreaseBalance")
    public StudentChecking decreaseBalance(@PathVariable long id, @RequestBody Money subtractedBalance){
        return studentCheckingService.decreaseBalance(subtractedBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/accounts/{username}/checking/student/{id}")
    public StudentChecking update(@PathVariable long id, @RequestBody StudentChecking studentCheckingAccount){
        return studentCheckingService.update(studentCheckingAccount, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/{username}/checking/student/{id}")
    public void delete (@PathVariable long id){
        if (studentCheckingRepository.findById(id).isPresent()){
            studentCheckingService.delete(id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect account id");
        }
    }

}