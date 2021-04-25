package com.github.sropelinen.olioht;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/* SQLite database structure.
* "users" table has 3 columns: name, password and data.
* Password and data are encrypted byte arrays, name is primary key. */

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "name")
    @NonNull
    private final String name;

    @ColumnInfo(name = "password", typeAffinity = ColumnInfo.BLOB)
    @NonNull
    private final byte[] password;

    @ColumnInfo(name = "data", typeAffinity = ColumnInfo.BLOB)
    private byte[] data;

    public User(@NonNull String name, @NonNull byte[] password) {
        this.name = name;
        this.password = password;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull byte[] getPassword() {
        return password;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
