package com.hiskysat.ports.spi;

import com.hiskysat.data.AccountDto;

public interface AccountPersistencePort {

    AccountDto addAccount(AccountDto accountDto);
    AccountDto getCurrentAccount();
}
