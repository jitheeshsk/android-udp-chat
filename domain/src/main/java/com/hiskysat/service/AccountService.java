package com.hiskysat.service;

import com.hiskysat.data.AccountDto;
import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.ports.spi.AccountPersistencePort;

public class AccountService implements AccountServicePort {

    private final AccountPersistencePort accountPersistencePort;

    public AccountService(AccountPersistencePort accountPersistencePort) {
        this.accountPersistencePort = accountPersistencePort;
    }

    @Override
    public AccountDto getCurrentUser() {
        return null;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        return null;
    }


    @Override
    public AccountDto getUser(String userName) {
        return null;
    }

    @Override
    public AccountDto updateUser(AccountDto accountDto) {
        return null;
    }
}
