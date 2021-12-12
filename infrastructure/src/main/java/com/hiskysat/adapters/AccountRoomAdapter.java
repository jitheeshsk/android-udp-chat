package com.hiskysat.adapters;

import android.content.Context;

import com.hiskysat.AppDatabase;
import com.hiskysat.data.AccountDto;
import com.hiskysat.ports.spi.AccountPersistencePort;
import com.hiskysat.repository.AccountsDao;

public class AccountRoomAdapter implements AccountPersistencePort {

    private final AccountsDao dao;

    public AccountRoomAdapter(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        this.dao = database.accountsDao();
    }

    @Override
    public AccountDto addAccount(AccountDto accountDto) {
        return null;
    }

    @Override
    public AccountDto getCurrentAccount() {
        return null;
    }
}
