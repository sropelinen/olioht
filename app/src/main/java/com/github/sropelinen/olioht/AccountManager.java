package com.github.sropelinen.olioht;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.HashMap;

public class AccountManager {

    private static AccountManager INSTANCE;
    private final UserDao userDao;

    public static AccountManager getManager(Context context) {
        if (INSTANCE == null) INSTANCE = new AccountManager(context);
        return INSTANCE;
    }

    private AccountManager(Context context) {
        userDao = AccountDatabase.getDatabase(context).userDao();
    }

    /* Database functions can't be performed on the main thread.
    *  execute(() -> { }) makes asynchronizing easier. */
    private void execute(Runnable r) {
        new Thread(r).start();
    }

    /* Adds new user to the database and switches to main activity.
    *  Runs fallback runnable on main thread if problem occurs. */
    public void addUser(String name, String password, HashMap<String, Object> values, Runnable fallback, Context context) {
        Handler handler = new Handler();
        execute(() -> {
            // Checks if the user already exists
            if (userDao.getUser(name) != null) {
                handler.post(fallback);
                return;
            }
            // Creates new user to the database
            User newUser = new User(name, Crypto.hashPassword(password, null));
            userDao.insert(newUser);
            // Initializes profile instance
            Profile profile = Profile.init("{}");
            profile.setSaver(() -> execute(() -> save(newUser, profile.getData(), password)));
            // Saves register information to database
            profile.setValues(values);
            save(newUser, profile.getData(), password);
            // Switch to main activity
            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
            ((LoginActivity) context).finish();
        });
    }

    /* Tries to login to a user account and switches to main activity.
    *  Runs fallback runnable on main thread if problem occurs. */
    public void login(String name, String password, Runnable fallback, Context context) {
        Handler handler = new Handler();
        execute(() -> {
            User user = userDao.getUser(name);
            if (user != null && Crypto.checkPassword(user.getPassword(), password)) {
                // Initializes profile instance
                Profile profile = Profile.init(Crypto.decryptData(password, user.getData()));
                profile.setSaver(() -> execute(() -> save(user, profile.getData(), password)));
                // Switch to main activity
                context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                ((LoginActivity) context).finish();
            } else {
                handler.post(fallback);
            }
        });
    }

    /* Encrypts data and updates user database. */
    private void save(User user, String data, String password) {
        user.setData(Crypto.encryptData(password, data));
        userDao.update(user);
    }
}
