package com.midterm.MyBank.service.utils;

import com.midterm.MyBank.controller.dto.AccountDTO;


import java.util.Set;

public interface YourHomePageService {
    Set<AccountDTO> get(String username);
}
