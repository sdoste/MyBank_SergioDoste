package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.Users.ThirdParty;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.repository.security.UserRepository;
import com.midterm.MyBank.service.users.ThirdPartyService;
import com.midterm.MyBank.service.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;
    @Autowired
    UserRepository userRepository;

    @PreAuthorize("(#username == principal.username AND hasRole('THIRDPARTY')) OR hasRole('ADMIN')")
    @PatchMapping("/thirdparty/{username}/{hashedKey}/deposit/{id}/{accountSecretKey}")
    public void deposit(@PathVariable String username, @PathVariable String hashedKey, @PathVariable long id,
                        @PathVariable String accountSecretKey, @RequestBody Money money) {
        ThirdParty thirdPartyUser = (ThirdParty) userRepository.findByUsername(username).get();
//        if (Objects.equals(hashedKey, PasswordUtil.encryptPassword(thirdPartyUser.getSecurityKey()))) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(thirdPartyUser.getSecurityKey(), hashedKey)) {
            //hashed key from http is the same as the password encrypted
            thirdPartyService.deposit(id, accountSecretKey, money);
        } else {
            //incorrect hashed key
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid key from third party:" + hashedKey + "|" + PasswordUtil.encryptPassword(thirdPartyUser.getSecurityKey()));
        }
    }

    @PreAuthorize("(#username == principal.username AND hasRole('THIRDPARTY')) OR hasRole('ADMIN')")
    @PatchMapping("/thirdparty/{username}/{hashedKey}/withdraw/{id}/{accountSecretKey}")
    public void withdraw (@PathVariable String username, @PathVariable String hashedKey,@PathVariable long id,
                          @PathVariable String accountSecretKey, @RequestBody Money money){
        ThirdParty thirdPartyUser = (ThirdParty) userRepository.findByUsername(username).get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(thirdPartyUser.getSecurityKey(), hashedKey)) {
            thirdPartyService.withdraw(id, accountSecretKey, money);
        } else {
            //incorrect hashed key
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid key from third party");
        }
    }

}