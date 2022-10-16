package com.midterm.MyBank.service.users;

import com.midterm.MyBank.model.Utils.Money;


public interface ThirdPartyService {
    void deposit(String username, String hashedKey, Long recipientAccountId, String secretKey, Money money);
    void withdraw(String username, String hashedKey, Long recipientAccountId, String secretKey, Money money);
//    ThirdParty get(String username, long id);
//    Account save(ThirdParty thirdPartyUser);
//    ThirdParty update(ThirdParty thirdPartyUser, long id);
//    void delete(ThirdParty thirdPartyUser);
}
