package com.hiskysat.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;

import java.util.UUID;

@Entity(tableName = "Accounts")
public final class Account {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "account_id")
    private final String id;

    @NonNull
    @ColumnInfo(name = "username")
    private final String username;

    @NonNull
    @ColumnInfo(name = "password")
    private final String password;

    @Ignore
    public Account(@NonNull String username, @NonNull String password) {
        this(UUID.randomUUID().toString(), username, password);
    }

    public Account(@NonNull String id, @NonNull String username, @NonNull String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equal(id, account.id) &&
                Objects.equal(username, account.username) &&
                Objects.equal(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username, password);
    }
}
