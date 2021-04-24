package com.github.sropelinen.olioht;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    private static AccountDatabase INSTANCE;

    public static AccountDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AccountDatabase.class, "accounts")
                    .setJournalMode(JournalMode.TRUNCATE)
                    .build();
        }
        return INSTANCE;
    }
}

