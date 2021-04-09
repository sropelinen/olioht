package com.github.sropelinen.olioht;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "name")
    @NonNull
    private final String name;

    @ColumnInfo(name = "password")
    @NonNull
    private final String password;

    public User(@NonNull String name, @NonNull String password) {
        this.name = name;
        this.password = password;
    }

    public @NonNull String getName() {
        return name;
    }
    public @NonNull String getPassword() {
        return password;
    }
}
