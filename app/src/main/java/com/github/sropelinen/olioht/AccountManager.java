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

    private void execute(Runnable r) {
        new Thread(r).start();
    }

    public void addUser(String name, String password, HashMap<String, Object> values, Runnable fallback, Context context) {
        Handler handler = new Handler();
        execute(() -> {
            if (userDao.getUser(name) != null) {
                handler.post(fallback);
                return;
            }
            User newUser = new User(name, Crypto.hashPassword(password, null));
            userDao.insert(newUser);
            Profile profile = Profile.init("{}");
            profile.setSaver(() -> execute(() -> save(newUser, profile.getData(), password)));
            profile.setValues(values);
            save(newUser, profile.getData(), password);
            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
            ((LoginActivity) context).finish();
        });
    }

    public void login(String name, String password, Runnable fallback, Context context) {
        Handler handler = new Handler();
        execute(() -> {
            User user = userDao.getUser(name);
            if (user != null && Crypto.checkPassword(user.getPassword(), password)) {
                Profile profile = Profile.init(Crypto.decryptData(password, user.getData()));
                profile.setSaver(() -> execute(() -> save(user, profile.getData(), password)));
                context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                ((LoginActivity) context).finish();
            } else {
                handler.post(fallback);
            }
        });
    }

    private void save(User user, String data, String password) {
        user.setData(Crypto.encryptData(password, data));
        userDao.update(user);
    }
}
