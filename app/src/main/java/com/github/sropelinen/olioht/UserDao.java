package com.github.sropelinen.olioht;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/* Methods to access SQLite database. */

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE name LIKE :userName")
    User getUser(String userName);

}
