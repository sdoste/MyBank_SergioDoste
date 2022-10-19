package com.midterm.MyBank.controller;

import com.midterm.MyBank.controller.dto.CreditCardDTO;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.CreditCard;
import com.midterm.MyBank.repository.CreditCardRepository;
import com.midterm.MyBank.service.accounts.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestController
public class CreditCardController {
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    CreditCardRepository creditCardRepository;

    //USER ACTIONS
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}/creditcard/{id}")
    public CreditCard get(@PathVariable String username, @PathVariable long id){
        if (creditCardRepository.findById(id).isPresent() &&
                (Objects.equals(creditCardRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return creditCardService.get(username, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @PatchMapping("/accounts/{username}/creditcard/{id}/transferTo/{recipientId}")
    public CreditCard transfer(@PathVariable String username, @PathVariable long id, @PathVariable long recipientId, @RequestBody Money money){
        if (creditCardRepository.findById(id).isPresent() &&
                (Objects.equals(creditCardRepository.findById(id).get().getPrimaryOwner().getUsername(), username))) {
            return creditCardService.transfer(id, recipientId, money);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account id not found for this username");
        }
    }
    //ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts/creditcard")
    public CreditCard create(@RequestBody CreditCardDTO creditCardDTO){
        return creditCardService.save(creditCardDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/creditcard/{id}/interestDate/{newDate}")
    public CreditCard updateLastAppliedInterestRate(@PathVariable long id, @PathVariable String newDate){
        if (creditCardRepository.findById(id).isPresent()){
            //account exists
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(newDate, formatter);
            } catch(Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date format");
            }
            return creditCardService.updateLastAppliedInterestRate(localDate, id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit card account does not exist");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/creditcard/{id}")
    public void delete (@PathVariable long id){
        if (creditCardRepository.findById(id).isPresent()){
            creditCardService.delete(id);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect account id");
        }
    }
}
