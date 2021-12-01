package com.hiskysat.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.Date;

@Entity(tableName = "chats")
public class Chat {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final Long id;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    @ColumnInfo(name = "user_id")
    private final Long userId;

    @NonNull
    @ColumnInfo(name = "created_on")
    private final Date  dateTime;

    @Ignore
    public Chat(String name, Long userId) {
        this(null, name, userId, new Date());
    }

    public Chat(@Nullable Long id, @NonNull String name, @NonNull Long userId, @NonNull Date dateTime) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    @NonNull
    public Date getDateTime() {
        return dateTime;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
