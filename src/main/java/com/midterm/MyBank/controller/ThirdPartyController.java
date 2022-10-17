package com.midterm.MyBank.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.service.users.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PreAuthorize("(#username == principal.username AND hasRole('THIRDPARTY')) OR hasRole('ADMIN')")
    @PatchMapping("/thirdparty/{username}/{key}/deposit/{id}")
    public void deposit(@PathVariable String username, @PathVariable String key, @PathVariable long id, @RequestBody String accountSecretKey, @RequestBody Money money) {
        thirdPartyService.deposit(username, key, id, accountSecretKey, money);
    }
    @PreAuthorize("(#username == principal.username AND hasRole('THIRDPARTY')) OR hasRole('ADMIN')")
    @PatchMapping("/thirdparty/{username}/{key}/withdraw/{id}")
    public void withdraw(@PathVariable String username, @PathVariable String key, @PathVariable long id, @RequestBody String accountSecretKey, @RequestBody Money money) {
        thirdPartyService.withdraw(username, key, id, accountSecretKey, money);
    }
}
