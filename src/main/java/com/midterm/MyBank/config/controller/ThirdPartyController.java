package com.midterm.MyBank.config.controller;

import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Account;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.service.accounts.interfaces.CheckingService;
import com.midterm.MyBank.service.users.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PreAuthorize("#username == principal.username OR hasRole('admin')")
    @GetMapping("/thirdparty/{username}/{key}/transfer/{id}")
    public Money get(@PathVariable String username, @PathVariable String key, @PathVariable long id, @RequestBody String accountSecretKey, @RequestBody Money money) {
        return thirdPartyService.transfer(username, key, id, accountSecretKey, money);
    }
}
