package com.hiskysat.repository;


import androidx.room.Dao;
import androidx.room.Query;

import com.hiskysat.entity.Account;

@Dao
public interface AccountsDao {

    @Query("SELECT * FROM Accounts")
    Account getCurrentAccount();


}

