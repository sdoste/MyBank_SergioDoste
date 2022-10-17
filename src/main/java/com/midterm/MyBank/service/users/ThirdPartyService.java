package com.midterm.MyBank.service.users;

import com.midterm.MyBank.model.Utils.Money;


public interface ThirdPartyService {
    void deposit(long recipientAccountId, String secretKey, Money money);
    void withdraw(long recipientAccountId, String secretKey, Money money);
//    ThirdParty get(String username, long id);
//    Account save(ThirdParty thirdPartyUser);
//    ThirdParty update(ThirdParty thirdPartyUser, long id);
//    void delete(ThirdParty thirdPartyUser);
}
