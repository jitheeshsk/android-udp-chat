package com.hiskysat.ports.api;

import com.hiskysat.data.AccountDto;

public interface AccountServicePort {

    AccountDto getCurrentUser();
    AccountDto createAccount(AccountDto accountDto);
    AccountDto getUser(String userName);
    AccountDto updateUser(AccountDto accountDto);

}
