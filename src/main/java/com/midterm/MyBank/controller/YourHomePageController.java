package com.midterm.MyBank.controller;

import com.midterm.MyBank.controller.dto.AccountDTO;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.utils.YourHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

//Accountholders obtain general info from their accounts
@RestController
public class YourHomePageController {

    @Autowired
    YourHomePageService yourHomePageService;
    @Autowired
    UserRepository userRepository;

    @PreAuthorize("#username == principal.username OR hasRole('ADMIN')")
    @GetMapping("/accounts/{username}")
    public Set<AccountDTO> get(@PathVariable String username){
        if (userRepository.findByUsername(username).isPresent()) {
            return yourHomePageService.get(username);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username does not exist");
        }
    }
}
