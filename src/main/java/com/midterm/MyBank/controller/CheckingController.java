package com.midterm.MyBank.controller;

import com.midterm.MyBank.controller.dto.CheckingDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingService;

    @Autowired
    CheckingRepository checkingRepository;

    //ACCOUNTHOLDER
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/checking/{id}")
    public Checking get(@PathVariable String username, @PathVariable long id){
        if (checkingRepository.findById(id).isPresent() &&
                (Objects.equals(checkingRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return checkingService.get(username, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}checking/{id}/transfer")
    public Checking transfer(@PathVariable String username, @PathVariable long id, @RequestBody long recipientId, @RequestBody Money money){
        if (checkingRepository.findById(id).isPresent() &&
                (Objects.equals(checkingRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return checkingService.transfer(id, recipientId, money);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    //ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/checking")
    public Account create(@RequestBody CheckingDTO checkingDTO){
        return checkingService.save(checkingDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/{id}/modifyBalance")
    public Checking modifyBalance(@PathVariable long id, @RequestBody Money newBalance){
        return checkingService.modifyBalance(newBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/{id}/increaseBalance")
    public Checking increaseBalance(@PathVariable long id, @RequestBody Money addedBalance){
        return checkingService.increaseBalance(addedBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/{id}/decreaseBalance")
    public Checking decreaseBalance(@PathVariable long id, @RequestBody Money subtractedBalance){
        return checkingService.decreaseBalance(subtractedBalance, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/checking/{id}")
    public Checking update(@PathVariable long id, @RequestBody Checking checkingAccount){
        return checkingService.update(checkingAccount, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/checking/{id}")
    public void delete (@PathVariable long id, @RequestBody Checking checkingAccount){
        checkingService.delete(checkingAccount);
    }
}
