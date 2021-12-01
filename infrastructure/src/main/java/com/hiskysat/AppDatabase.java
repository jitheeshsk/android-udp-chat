package com.hiskysat;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hiskysat.entity.Account;
import com.hiskysat.entity.Chat;
import com.hiskysat.entity.Message;
import com.hiskysat.entity.convertors.DateConvertors;
import com.hiskysat.repository.AccountsDao;

@Database(entities = {Account.class, Chat.class, Message.class }, version = 1,exportSchema = false)
@TypeConverters({DateConvertors.class})
public abstract class AppDatabase extends RoomDatabase  {

    private static AppDatabase INSTANCE;

    public abstract AccountsDao accountsDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "UDPChats.db")
                        .build();
            }

            return INSTANCE;
        }
    }


}
