package com.hiskysat.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final Long id;

    @NonNull
    @ColumnInfo(name = "chat_id")
    private final Long chatId;

    @NonNull
    @ColumnInfo(name = "message")
    private final String message;

    @NonNull
    @ColumnInfo(name = "created_at")
    private final Date dateTime;

    @Ignore
    public Message(@NonNull Long chatId, @NonNull String message) {
        this(null, chatId, message, new Date());
    }

    public Message(@Nullable Long id, @NonNull Long chatId, @NonNull String message, @NonNull Date dateTime) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.dateTime = dateTime;
    }


    public Long getId() {
        return id;
    }

    @NonNull
    public Long getChatId() {
        return chatId;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public Date getDateTime() {
        return dateTime;
    }
}
